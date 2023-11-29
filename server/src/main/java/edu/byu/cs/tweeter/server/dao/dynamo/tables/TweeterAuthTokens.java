package edu.byu.cs.tweeter.server.dao.dynamo.tables;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class TweeterAuthTokens implements DynamoTweeterTable {
//////// CONSTANTS ////////
    /**
     * The name of the table in DynamoDB that contains TweeterAuthTokens items
     */
    private static final String TABLE_NAME = "TweeterAuthTokens";

    /**
     *  The name of the primary index for the TweeterAuthTokens DynamoDB table.
     */
    private static final String INDEX_NAME = "token"; //TODO: add index name

    /**
     *  The name of the attribute that is the partition key of primary index for the TweeterAuthTokens DynamoDB table.
     */
    private static final String INDEX_ATTRIBUTE_NAME = "token";



//////// ATTRIBUTES ////////
    private String token; // primary partition key
    private Number expiresTimestamp;
    private String expiresTimestampString;
    private Number expiresOffsetInSeconds;


    
//////// CONSTRUCTOR ////////
    public TweeterAuthTokens(String token, Number expiresTimestamp, String expiresTimestampString, Number expiresOffsetInSeconds) {
        this.token = token;
        this.expiresTimestamp = expiresTimestamp;
        this.expiresTimestampString = expiresTimestampString;
        this.expiresOffsetInSeconds = expiresOffsetInSeconds;
    }

    
//////// GETTERS AND SETTERS ////////
    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Number getExpiresTimestamp() {
        return expiresTimestamp;
    }

    public void setExpiresTimestamp(Number expiresTimestamp) {
        this.expiresTimestamp = expiresTimestamp;
    }

    public String getExpiresTimestampString() {
        return expiresTimestampString;
    }

    public void setExpiresTimestampString(String expiresTimestampString) {
        this.expiresTimestampString = expiresTimestampString;
    }

    public Number getExpiresOffsetInSeconds() {
        return expiresOffsetInSeconds;
    }

    public void setExpiresOffsetInSeconds(Number expiresOffsetInSeconds) {
        this.expiresOffsetInSeconds = expiresOffsetInSeconds;
    }



//////// DynamoTweeterTable METHODS ////////
    /**
     * Gets the name of the table in DynamoDB that contains TweeterAuthTokens items.
     * @return the name of the table in DynamoDB that contains TweeterAuthTokens items
     */
    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    /**
     * Gets the name of the primary index (partition key) for the TweeterAuthTokens DynamoDB table.
     * @return the name of the primary index for the DynamoDB table.
     */
    @Override
    public String indexName() {
        return INDEX_NAME;
    }

    /**
     * Gets the name of the attribute that is set to be the primary index key (partition key) for the TweeterAuthTokens DynamoDB table.
     * @return the name of the attribute that is set to be the primary index/partition key for the TweeterAuthTokens table.
     */
    @Override
    public String indexAttributeName() {
        return INDEX_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the attribute that is the sort key for the primary index of the TweeterAuthTokens DynamoDB table. Because TweeterAuthTokens does not use a sort key, this will return null.
     * @return null (TweeterAuthTokens does not use a primary sort key)
     */
    @Override
    public String sortAttributeName() {
        return null;
    }

    /**
     * Gets the name of the secondary index for the TweeterAuthTokens DynamoDB table. Because TweeterAuthTokens does not use a secondary index, this will return null.
     * @return null (TweeterAuthTokens does not have a secondary index)
     */
    @Override
    public String secondaryIndexName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the index key for secondary index of the TweeterAuthTokens DynamoDB table. Because TweeterAuthTokens does not use a secondary index, this will return null.
     * @return null (TweeterAuthTokens does not have a secondary index)
     */
    @Override
    public String secondaryIndexAttributeName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the sort key for the secondary index of the TweeterAuthTokens DynamoDB table. Because TweeterAuthTokens does not use a secondary index, this will return null.
     * @return null (TweeterAuthTokens does not have a secondary sort key)
     */
    @Override
    public String secondarySortAttributeName() {
        return null;
    }
}
