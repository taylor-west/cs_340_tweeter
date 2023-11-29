package edu.byu.cs.tweeter.server.dao.dynamo;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterAuthTokens;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * A DAO for accessing 'authtoken' data from the database.
 */
public class DynamoAuthTokenDAO extends DynamoDAO<TweeterAuthTokens> implements AuthTokenDAO {
    private static final int DEFAULT_AUTHTOKEN_EXPIRATION_OFFSET_SECONDS = (15 * 60); // 15 minutes
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    private static final Class<TweeterAuthTokens> AUTHTOKEN_TABLE_CLASS = TweeterAuthTokens.class;
    private static final String AUTHTOKEN_TABLE_NAME = "TweeterAuthTokens";
    private static final String AUTHTOKEN_TIMESTAMP_ATTRIBUTE_NAME = "expiresTimestamp";
    private static final String AUTHTOKEN_TIMESTAMP_STRING_ATTRIBUTE_NAME = "expiresTimestampString";
    private static final String AUTHTOKEN_OFFSET_ATTRIBUTE_NAME = "expiresOffsetInSeconds";
    private static final String AUTHTOKEN_INDEX_ATTRIBUTE_NAME = "token";

    private static DynamoDbTable<TweeterAuthTokens> tweeterAuthTokensDynamoDbTable;

    /**
     * Creates a new AuthToken and adds it to the database.
     */
    @Override
    public AuthToken addAuthToken(int authTokenLifespanInSeconds) {
        //insert(AUTHTOKEN_TABLE_NAME, AUTHTOKEN_TABLE_CLASS, authToken);

        AuthToken newAuthToken = new AuthToken(generateNewToken(), Instant.now().getEpochSecond());
        try{
            DynamoDbTable<TweeterAuthTokens> table = getTweeterAuthTokensTable();
            Key key = Key.builder()
                    .partitionValue(newAuthToken.getToken())
                    .build();

            // check that the token doesn't already exist
            TweeterAuthTokens existingEntry = table.getItem(key);
            if (existingEntry == null) {
                long expiresTimestamp = newAuthToken.getTimestamp() + authTokenLifespanInSeconds;
                TweeterAuthTokens newTweeterAuthToken = new TweeterAuthTokens(newAuthToken.getToken(), expiresTimestamp, Instant.ofEpochSecond(expiresTimestamp).toString(), authTokenLifespanInSeconds);
                table.putItem(newTweeterAuthToken);
            }
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while inserting AuthToken into database (partitionValue=" + newAuthToken.getToken() + "): " + e.getMessage());
        }

        return newAuthToken;
    }

    /**
     * Set the timestamp of the given AuthToken to the current time. This effectively "refreshes"
     * the window of validity for the AuthToken, extending its life. ONLY WORKS FOR EXISTING AUTHTOKENS.
     *
     * @param authToken the AuthToken whose timestamp will be updated
     */
    @Override
    public void refreshAuthToken(AuthToken authToken) {
//        update(AUTHTOKEN_TABLE_NAME, AUTHTOKEN_TABLE_CLASS, authToken);

        try{
            DynamoDbTable<TweeterAuthTokens> table = getTweeterAuthTokensTable();
            Key key = Key.builder()
                    .partitionValue(authToken.getToken())
                    .build();

            // checks if it already exists
            TweeterAuthTokens existingEntry = table.getItem(key);
            if (existingEntry != null) {
                // calculate new values
                int offsetSeconds = existingEntry.getExpiresOffsetInSeconds().intValue();
                Instant newTime = Instant.now();
                long newExpiresTimestamp = (newTime.getEpochSecond() + offsetSeconds);
                String newExpiresTimestampString = newTime.toString();

                // assign new values to the local object
                existingEntry.setExpiresTimestamp(newExpiresTimestamp);
                existingEntry.setExpiresTimestampString(newExpiresTimestampString);

                // update database with modified local object
                TweeterAuthTokens updatedItem = table.updateItem(existingEntry);
            }
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while updating AuthToken in database (partitionValue=" + authToken.getToken() + "): " + e.getMessage());
        }
    }


    @Override
    public boolean verifyAuthTokenIsValid(AuthToken authToken) {
        boolean isValid = false;
        //AuthToken dbAuthToken = find(AUTHTOKEN_TABLE_NAME, AUTHTOKEN_TABLE_CLASS, authToken.partitionValue(), null);


        TweeterAuthTokens dbAuthToken = null;
        try{
            DynamoDbTable<TweeterAuthTokens> table = getTweeterAuthTokensTable();
            Key key = Key.builder()
                    .partitionValue(authToken.getToken())
                    .build();

            // get TweeterAuthToken from database
            dbAuthToken = table.getItem(key);

            if( (dbAuthToken != null) && (!timestampIsExpired( dbAuthToken.getExpiresTimestamp() )) ){
                isValid = true;
            }

            return isValid;
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while verifying validity of AuthToken (partitionValue=" + authToken.getToken() + "): " + e.getMessage());
        }
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) {
//        delete(AUTHTOKEN_TABLE_NAME, AUTHTOKEN_TABLE_CLASS, authToken.partitionValue(), null);


        try{
            DynamoDbTable<TweeterAuthTokens> table = getTweeterAuthTokensTable();
            Key key = Key.builder()
                    .partitionValue(authToken.getToken())
                    .build();

            // deletes TweeterAuthToken from the database
            TweeterAuthTokens deletedItem = table.deleteItem(key);
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while deleting AuthToken from database (partitionValue=" + authToken.getToken() + "): " + e.getMessage());
        }
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private boolean timestampIsExpired(Number timestamp){
        Instant timestampInstant = Instant.ofEpochSecond(timestamp.longValue());
        boolean isExpired = Instant.now().isAfter( timestampInstant );
        return isExpired;
    }

    /**
     * Gets a singleton instance of the TweeterAuthTokens DynamoDbTable table.
     * @return an instance of the TweeterAuthTokens DynamoDbTable table.
     */
    private DynamoDbTable<TweeterAuthTokens> getTweeterAuthTokensTable(){
        if(tweeterAuthTokensDynamoDbTable == null){
            tweeterAuthTokensDynamoDbTable = enhancedClient.table(AUTHTOKEN_TABLE_NAME, TableSchema.fromBean(AUTHTOKEN_TABLE_CLASS));
        }

        return tweeterAuthTokensDynamoDbTable;
    }
}
