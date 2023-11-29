package edu.byu.cs.tweeter.server.dao.dynamo.tables;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class TweeterUsers implements DynamoTweeterTable {
//////// CONSTANTS ////////
    /**
     * The name of the table in DynamoDB that contains TweeterUsers items
     */
    private static final String TABLE_NAME = "TweeterUsers";

    /**
     *  The name of the primary index for the TweeterUsers DynamoDB table.
     */
    private static final String INDEX_NAME = "alias";

    /**
     *  The name of the attribute that is the partition key of primary index for the TweeterUsers DynamoDB table.
     */
    private static final String INDEX_ATTRIBUTE_NAME = "alias";



//////// ATTRIBUTES ////////
    private String alias; // primary partition key
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String imageURL;
    private int followersCount;
    private int followingCount;



//////// CONSTRUCTOR ////////
    public TweeterUsers(String alias, String firstName, String lastName, String passwordHash, String imageURL) {
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.imageURL = imageURL;
        this.followersCount = 0;
        this.followingCount = 0;
    }


//////// GETTERS AND SETTERS ////////
    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    //////// DynamoTweeterTable METHODS ////////
    /**
     * Gets the name of the table in DynamoDB that contains TweeterUsers items.
     * @return the name of the table in DynamoDB that contains TweeterUsers items
     */
    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    /**
     * Gets the name of the primary index (partition key) for the TweeterUsers DynamoDB table.
     * @return the name of the primary index for the DynamoDB table.
     */
    @Override
    public String indexName() {
        return INDEX_NAME;
    }

    /**
     * Gets the name of the attribute that is set to be the primary index key (partition key) for the TweeterUsers DynamoDB table.
     * @return the name of the attribute that is set to be the primary index/partition key for the TweeterUsers table.
     */
    @Override
    public String indexAttributeName() {
        return INDEX_ATTRIBUTE_NAME;
    }

    /**
     * Gets the name of the attribute that is the sort key for the primary index of the TweeterUsers DynamoDB table. Because TweeterUsers does not use a sort key, this will return null.
     * @return null (TweeterUsers does not use a primary sort key)
     */
    @Override
    public String sortAttributeName() {
        return null;
    }

    /**
     * Gets the name of the secondary index for the TweeterUsers DynamoDB table. Because TweeterUsers does not use a secondary index, this will return null.
     * @return null (TweeterUsers does not have a secondary index)
     */
    @Override
    public String secondaryIndexName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the index key for secondary index of the TweeterUsers DynamoDB table. Because TweeterUsers does not use a secondary index, this will return null.
     * @return null (TweeterUsers does not have a secondary index)
     */
    @Override
    public String secondaryIndexAttributeName() {
        return null;
    }

    /**
     * Gets the name of the attribute that is the sort key for the secondary index of the TweeterUsers DynamoDB table. Because TweeterUsers does not use a secondary index, this will return null.
     * @return null (TweeterUsers does not have a secondary sort key)
     */
    @Override
    public String secondarySortAttributeName() {
        return null;
    }


//////// DynamoTweeterTable METHODS ////////
    public User convertToUser(){
        User equivalentUser = new User(getFirstName(), getLastName(), getAlias(), getImageURL());
        return equivalentUser;
    }

}
