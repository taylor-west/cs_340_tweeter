package edu.byu.cs.tweeter.server.dao.dynamo.tables;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class TweeterFollows implements DynamoTweeterTable {
    // https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html


//////// CONSTANTS ////////
    /**
     * The name of the table in DynamoDB that contains TweeterAuthTokens items
     */
    private static final String TABLE_NAME = "TweeterFollows";


    /**
     *  The name of the primary index for the TweeterFollows table.
     */
    public static final String INDEX_NAME = "followerAlias";

    /**
     *  The name of the attribute that is the partition key of primary index for the TweeterFollows DynamoDB table.
     */
    private static final String INDEX_ATTRIBUTE_NAME = "followerAlias";

    /**
     *  The name of the attribute that is set to be a sort key for the primary index of the DynamoDB table.
     */
    private static final String SORT_ATTRIBUTE_NAME = "followeeAlias";


    /**
     *  The name of the secondary index for the TweeterFollows table.
     */
    private static final String SECONDARY_INDEX_NAME = "followeeAlias-followerAlias-index";

    /**
     *  The name of the attribute that is set to be a secondary index for the DynamoDB table.
     */
    private static final String SECONDARY_INDEX_ATTRIBUTE_NAME = "followeeAlias";

    /**
     *  The name of the attribute that is set to be a sort key for the secondary index of the DynamoDB table.
     */
    private static final String SECONDARY_SORT_ATTRIBUTE_NAME = "followerAlias";



//////// ATTRIBUTES ////////
    private String followerAlias; // primary partition key, secondary sort key
    private String followerFirstName;
    private String followerLastName;
    private String followerImageURL;
    private String followeeAlias; // primary sort key, secondary partition key
    private String followeeFirstName;
    private String followeeLastName;
    private String followeeImageURL;


    //////// GETTERS AND SETTERS ////////
    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = SECONDARY_INDEX_NAME)
    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFollowerFirstName() {
        return followerFirstName;
    }

    public void setFollowerFirstName(String followerFirstName) {
        this.followerFirstName = followerFirstName;
    }

    public String getFollowerLastName() {
        return followerLastName;
    }

    public void setFollowerLastName(String followerLastName) {
        this.followerLastName = followerLastName;
    }

    public String getFollowerImageURL() {
        return followerImageURL;
    }

    public void setFollowerImageURL(String followerImageURL) {
        this.followerImageURL = followerImageURL;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = { SECONDARY_INDEX_NAME })
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getFolloweeFirstName() {
        return followeeFirstName;
    }

    public void setFolloweeFirstName(String followeeFirstName) {
        this.followeeFirstName = followeeFirstName;
    }

    public String getFolloweeLastName() {
        return followeeLastName;
    }

    public void setFolloweeLastName(String followeeLastName) {
        this.followeeLastName = followeeLastName;
    }

    public String getFolloweeImageURL() {
        return followeeImageURL;
    }

    public void setFolloweeImageURL(String followeeImageURL) {
        this.followeeImageURL = followeeImageURL;
    }


//////// DynamoTweeterTable METHODS ////////
    /**
     * Gets the name of the table in DynamoDB that contains the items being accessed.
     * @return the name of the DynamoDB table
     */
    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    /**
     * Gets the name of the primary index (partition key) for the DynamoDB table.
     * @return the name of the primary index for the DynamoDB table.
     */
    @Override
    public String indexName() {
        return INDEX_NAME;
    }

    /**
     * Gets the name of the attribute that is set to be the primary index key (partition key) for the DynamoDB table.
     * @return the name of the attribute that is set to be the primary index/partition key for the table.
     */
    @Override
    public String indexAttributeName() {
        return INDEX_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the attribute that is the sort key for the primary index of the DynamoDB table.
     * @return the name of the attribute that is the sort key for the primary index of the table.
     */
    @Override
    public String sortAttributeName() {
        return SORT_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the secondary index for the DynamoDB table.
     * @return the name of the secondary index for the table.
     */
    @Override
    public String secondaryIndexName() {
        return SECONDARY_INDEX_NAME;
    }

    /**
     * Gets the name of the attribute that is the index key for secondary index of the DynamoDB table.
     * @return the name of the attribute that is the index key for secondary index of the table.
     */
    @Override
    public String secondaryIndexAttributeName() {
        return SECONDARY_INDEX_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the attribute that is the sort key for the secondary index of the DynamoDB table.
     * @return the name of the attribute that is the sort key for the secondary index of the table.
     */
    @Override
    public String secondarySortAttributeName() {
        return SECONDARY_SORT_ATTRIBUTE_NAME;
    }



//////// ADDITIONAL METHODS ////////
    public User userFromFollowee(){
        User equivalentUser = new User( getFolloweeFirstName(), getFolloweeLastName(), getFolloweeAlias(), getFolloweeImageURL() );
        return equivalentUser;
    }

    public User userFromFollower(){
        User equivalentUser = new User( getFollowerFirstName(), getFollowerLastName(), getFollowerAlias(), getFollowerImageURL() );
        return equivalentUser;
    }
}
