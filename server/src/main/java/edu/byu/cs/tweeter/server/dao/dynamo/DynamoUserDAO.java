package edu.byu.cs.tweeter.server.dao.dynamo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterUsers;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class DynamoUserDAO extends DynamoDAO implements UserDAO {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private static final Class<TweeterUsers> UsersTableClass = TweeterUsers.class;
    private static final String UsersTableName = "TweeterUsers";
    private static final String UsersAliasAttribute = "alias";
    private static final String UsersPasswordHashAttribute = "passwordHash";
    private static final String UsersIndexName = "alias";

    private static DynamoDbTable<TweeterUsers> tweeterUsersDynamoDbTable;

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

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();
            Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            // get TweeterAuthToken from database
            TweeterUsers dbUser = table.getItem(key);

            if(dbUser == null){
                throw new RuntimeException("[AuthError] Invalid username (partitionValue/username=" + username + ")");
            }

            // check the given raw password against the encoded password stored in the database
            boolean passwordValid = passwordEncoder.matches(rawPassword, dbUser.getPasswordHash());

            if( !passwordValid ){
                throw new RuntimeException("[AuthError] Incorrect password (password=" + rawPassword + ")");
            }

            User loggedInUser = dbUser.convertToUser();
            return loggedInUser;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while verifying the password of a User (partitionValue/username=" + username + ", password=" + rawPassword + "): " + e.getMessage());
        }
    }


    /**
     * Creates a new User entry in the database if the login is valid.
     *
     * @param username the desired alias/username of the User whose is attempting to register
     * @param rawPassword the plain password of the User whose is attempting to register
     * @param firstName the first name of the User whose is attempting to register
     * @param lastName the last name of the User whose is attempting to register
     * @param imageURL a URL pointing to the S3 bucket where the desired profile image is stored.
     * @return a new User with the specified details
     */
    @Override
    public User register(String username, String rawPassword, String firstName, String lastName, String imageURL) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert username != null;
        assert rawPassword != null;
        assert firstName != null;
        assert lastName != null;
        assert imageURL != null;

        try{
            DynamoDbTable<TweeterUsers> table = getTweeterUsersTable();

            // encode the password for storage
            String encodedPassword = saltAndHashPassword(rawPassword);

            // create a new TweeterUsers record with appropriate information
            TweeterUsers newTweeterUser = new TweeterUsers(username, firstName, lastName, encodedPassword, imageURL);

            // add the TweeterUsers record to the database
            table.putItem(newTweeterUser);

            User registeredUser = newTweeterUser.convertToUser();
            return registeredUser;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while adding User to the database (username=" + username + ", password=" + rawPassword + ", firstName=" + firstName + ", lastName=" + lastName + ", imageURL=" + imageURL + "): " + e.getMessage());
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
            tweeterUsersDynamoDbTable = enhancedClient.table(UsersTableName, TableSchema.fromBean(UsersTableClass));
        }

        return tweeterUsersDynamoDbTable;
    }

    private String saltAndHashPassword(String rawPassword) {
        String saltedHashedPassword = passwordEncoder.encode(rawPassword);
        return saltedHashedPassword;
    }
}
