package edu.byu.cs.tweeter.server.dao.dynamo.tables;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class TweeterStories implements DynamoTweeterTable {
//////// CONSTANTS ////////
    /**
     * The name of the table in DynamoDB that contains TweeterStories items
     */
    private static final String TABLE_NAME = "TweeterStories";


    /**
     *  The name of the primary index for the TweeterStories table.
     */
    private static final String INDEX_NAME = "userAlias";

    /**
     *  The name of the attribute that is the partition key of primary index for the TweeterStories DynamoDB table.
     */
    private static final String INDEX_ATTRIBUTE_NAME = "userAlias";

    /**
     *  The name of the attribute that is set to be a sort key for the primary index of the DynamoDB table.
     */
    private static final String SORT_ATTRIBUTE_NAME = "timestamp";



//////// ATTRIBUTES ////////
    /**
     * The alias of the User who made the post/status
     */
    String userAlias; // primary partition key

    /**
     * Text for the status.
     */
    public String postText;

    /**
     * Number representation of the date/time at which the status was sent (use to compare).
     */
    public Number timestamp; // primary sort key

    /**
     * String representation of the date/time at which the status was sent.
     */
    public String timestampString;

    /**
     * URLs contained in the post text.
     */
    public List<String> urls;

    /**
     * User mentions contained in the post text.
     */
    public List<String> mentionedAliases;


//////// CONSTRUCTOR ////////

    public TweeterStories(String userAlias, String postText, Number timestamp, String timestampString, List<String> urls, List<String> mentionedAliases) {
        this.userAlias = userAlias;
        this.postText = postText;
        this.timestamp = timestamp;
        this.timestampString = timestampString;
        this.urls = urls;
        this.mentionedAliases = mentionedAliases;
    }


//////// GETTERS AND SETTERS ////////

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    @DynamoDbSortKey
    public Number getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Number timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampString() {
        return timestampString;
    }

    public void setTimestampString(String timestampString) {
        this.timestampString = timestampString;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentionedAliases() {
        return mentionedAliases;
    }

    public void setMentionedAliases(List<String> mentionedAliases) {
        this.mentionedAliases = mentionedAliases;
    }


//////// DynamoTweeterTable METHODS ////////
    /**
     * Gets the name of the table in DynamoDB that contains TweeterStories items.
     * @return the name of the table in DynamoDB that contains TweeterStories items
     */
    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    /**
     * Gets the name of the primary index (partition key) for the TweeterStories DynamoDB table.
     * @return the name of the primary index for the DynamoDB table.
     */
    @Override
    public String indexName() {
        return INDEX_NAME;
    }

    /**
     * Gets the name of the attribute that is set to be the primary index key (partition key) for the TweeterStories DynamoDB table.
     * @return the name of the attribute that is set to be the primary index/partition key for the TweeterStories table.
     */
    @Override
    public String indexAttributeName() {
        return INDEX_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the attribute that is the sort key for the primary index of the TweeterStories DynamoDB table. Because TweeterStories does not use a sort key, this will return null.
     * @return null (TweeterStories does not use a primary sort key)
     */
    @Override
    public String sortAttributeName() {
        return null;
    }

    /**
     * Gets the name of the secondary index for the TweeterStories DynamoDB table. Because TweeterStories does not use a secondary index, this will return null.
     * @return null (TweeterStories does not have a secondary index)
     */
    @Override
    public String secondaryIndexName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the index key for secondary index of the TweeterStories DynamoDB table. Because TweeterStories does not use a secondary index, this will return null.
     * @return null (TweeterStories does not have a secondary index)
     */
    @Override
    public String secondaryIndexAttributeName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the sort key for the secondary index of the TweeterStories DynamoDB table. Because TweeterStories does not use a secondary index, this will return null.
     * @return null (TweeterStories does not have a secondary sort key)
     */
    @Override
    public String secondarySortAttributeName() {
        return null;
    }

    //////// ADDITIONAL METHODS ////////
    public Status statusFromStory(User storyUser){
        Status equivalentStatus = new Status( getPostText(), storyUser, getTimestamp().longValue(), getUrls(), getMentionedAliases() );
        return equivalentStatus;
    }
}
