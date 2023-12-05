package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterUsers;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class DynamoUserDAO extends DynamoDAO<TweeterUsers> implements UserDAO {
    public static final String S3_IMAGE_BUCKET_NAME = "cs340-wtaylorh-tweeter-images";
    public static final Region S3_IMAGE_BUCKET_REGION = Region.US_WEST_1;
    private AmazonS3 s3;

    private static final Class<TweeterUsers> UsersTableClass = TweeterUsers.class;
    private static final String UsersTableName = "TweeterUsers";
//    private static final String UsersAliasAttribute = "alias";
//    private static final String UsersPasswordHashAttribute = "passwordHash";
//    private static final String UsersIndexName = "alias";

    private static DynamoDbTable<TweeterUsers> tweeterUsersDynamoDbTable;

    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Gets the user from the database with the alias specified. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param userAlias the alias/username of the User whose information we are trying to find
     * @return the user with the given alias.
     */
    @Override
    public User getUser(String userAlias) {
        assert userAlias != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(userAlias)
                    .build();

            // get the TweeterUsers record from database
            TweeterUsers dbUser = table.getItem(key);
            User existingUser = dbUser.convertToUser();
            return existingUser;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while getting User from database (partitionValue=" + userAlias + "): " + e.getMessage());
        }
    }

    /**
     * Compares the given username and password against the database to verify if the login is
     * valid.
     *
     * @param username the alias/username of the User whose is attempting to login
     * @param rawPassword the plain password of the User who is attempting to login
     * @return the whether the given username and password match
     */
    @Override
    public User login(String username, String rawPassword) {
        assert username != null;
        assert rawPassword != null;

        DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
        Key key = Key.builder()
                .partitionValue(username)
                .build();

        // get TweeterAuthToken from database
        TweeterUsers dbUser = table.getItem(key);

        if(dbUser == null){
            throw new RuntimeException("[Bad Request] Username does not exist (partitionValue/username=" + username + ")");
        }

        // check the given raw password against the encoded password stored in the database
        boolean passwordValid = getPasswordEncoder().matches(rawPassword, dbUser.getPasswordHash());

        if( !passwordValid ){
            throw new RuntimeException("[Bad Request] Incorrect password (password=" + rawPassword + ")");
        }

        User loggedInUser = dbUser.convertToUser();
        System.out.println("DynamoUserDAO.login: success! loggedInUser=" + loggedInUser.toString());
        return loggedInUser;
    }


    /**
     * Creates a new User entry in the database if the login is valid.
     *
     * @param username the desired alias/username of the User who is attempting to register
     * @param rawPassword the plain password of the User who is attempting to register
     * @param firstName the first name of the User who is attempting to register
     * @param lastName the last name of the User who is attempting to register
     * @param imageByteString a string representing the Base64 encoded profile image for the User
     *                        who is attempting to register
     * @return a new User with the specified details
     */
    @Override
    public User register(String username, String rawPassword, String firstName, String lastName, String imageByteString) {
        System.out.println("in DynamoUserDAO.register with params: " + "(username=" + username + ", password=" + rawPassword + ", firstName=" + firstName + ", lastName=" + lastName + ", imageURL=" + imageByteString + ")");
        assert username != null;
        assert rawPassword != null;
        assert firstName != null;
        assert lastName != null;
        assert imageByteString != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            System.out.println("  finished getting a DynamoDbTable<TweeterUsers> table");

            // uploads the image to the S3 bucket and returns the URL
            String imageURL = storeImage(imageByteString, username);
            System.out.println("  finished storing image");

            // encode the password for storage
            String encodedPassword = saltAndHashPassword(rawPassword);
            System.out.println("  finished encoding password");

            // create a new TweeterUsers record with appropriate information
            TweeterUsers newTweeterUser = new TweeterUsers();
            newTweeterUser.setAlias(username);
            newTweeterUser.setFirstName(firstName);
            newTweeterUser.setLastName(lastName);
            newTweeterUser.setPasswordHash(encodedPassword);
            newTweeterUser.setImageURL(imageURL);
            System.out.println("  finished creating new TweeterUser");
            // add the TweeterUsers record to the database
            table.putItem(newTweeterUser);
            System.out.println("  finished adding TweeterUser to database");
            User registeredUser = newTweeterUser.convertToUser();
            return registeredUser;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while adding User to the database (username=" + username + ", password=" + rawPassword + ", firstName=" + firstName + ", lastName=" + lastName + ", imageURL=" + imageByteString + "): " + e.getMessage());
        }
    }

    @Override
    public int getFollowingCount(String userAlias) {
        assert userAlias != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(userAlias)
                    .build();

            // get the TweeterUsers record from database
            TweeterUsers existingUser = table.getItem(key);
            int followingCount = existingUser.getFollowingCount();
            return followingCount;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while getting followingCount for User from database (partitionValue=" + userAlias + "): " + e.getMessage());
        }
    }

    @Override
    public int getFollowersCount(String userAlias) {
        assert userAlias != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(userAlias)
                    .build();

            // get the TweeterUsers record from database
            TweeterUsers existingUser = table.getItem(key);

            int followersCount = existingUser.getFollowersCount();
            return followersCount;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while getting followersCount for User from database (partitionValue=" + userAlias + "): " + e.getMessage());
        }
    }

    @Override
    public void changeFollowersCount(String userAlias, Integer amount) {
        assert userAlias != null;
        assert amount != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(userAlias)
                    .build();

            // get the TweeterUsers record from database
            TweeterUsers existingUser = table.getItem(key);

            int prevFollowersCount = existingUser.getFollowersCount();
            existingUser.setFollowersCount(prevFollowersCount + amount);
            TweeterUsers updatedUser = table.updateItem(existingUser);
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while updating followersCount for User in database (partitionValue=" + userAlias + ", amount=" + amount.toString() + "): " + e.getMessage());
        }
    }

    @Override
    public void changeFollowingCount(String userAlias, Integer amount) {
        assert userAlias != null;
        assert amount != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(userAlias)
                    .build();

            // get the TweeterUsers record from database
            TweeterUsers existingUser = table.getItem(key);

            int prevFollowingCount = existingUser.getFollowingCount();
            existingUser.setFollowingCount(prevFollowingCount + amount);
            TweeterUsers updatedUser = table.updateItem(existingUser);
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while updating followingCount for User in database (partitionValue=" + userAlias + ", amount=" + amount.toString() + "): " + e.getMessage());
        }
    }

    /**
     * Gets a singleton instance of the TweeterUsers DynamoDbTable table.
     * @return an instance of the TweeterUsers DynamoDbTable table.
     */
    private DynamoDbTable<TweeterUsers> getTweeterUsersTable(){

        if(tweeterUsersDynamoDbTable == null){
            tweeterUsersDynamoDbTable = getEnhancedDynamoClient().table(UsersTableName, TableSchema.fromBean(UsersTableClass));
        }

        return tweeterUsersDynamoDbTable;
    }

    private String saltAndHashPassword(String rawPassword) {
        String saltedHashedPassword =  getPasswordEncoder().encode(rawPassword);
        return saltedHashedPassword;
    }

    public String storeImage(String imageBytesString, String userAlias) {
        byte[] byteArray = Base64.getDecoder().decode(imageBytesString);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest(S3_IMAGE_BUCKET_NAME, userAlias, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        getS3().putObject(request);

        String imageURL = "https://" + S3_IMAGE_BUCKET_NAME + ".s3." + S3_IMAGE_BUCKET_REGION.toString() + ".amazonaws.com/" + userAlias;
        return imageURL;
    }

    private BCryptPasswordEncoder getPasswordEncoder(){
        if(this.passwordEncoder == null){
            this.passwordEncoder = new BCryptPasswordEncoder();
        }
        return this.passwordEncoder;
    }

    private AmazonS3 getS3(){
        if(this.s3 == null){
            System.out.println("creating new S3 client...");
            this.s3 =  AmazonS3ClientBuilder.standard().withRegion(S3_IMAGE_BUCKET_REGION.toString()).build();
            System.out.println("S3 client created");
        }
        return this.s3;
    }
}
