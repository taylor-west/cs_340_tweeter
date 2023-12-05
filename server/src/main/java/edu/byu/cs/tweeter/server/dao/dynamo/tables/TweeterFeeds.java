package edu.byu.cs.tweeter.server.dao.dynamo.tables;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class TweeterFeeds implements DynamoTweeterTable {
//////// CONSTANTS ////////
   /**
    * The name of the table in DynamoDB that contains TweeterAuthTokens items
    */
   private static final String TABLE_NAME = "TweeterFeeds";


   /**
    *  The name of the primary index for the TweeterFollows table.
    */
   private static final String INDEX_NAME = "feedOwnerAlias"; //TODO: add index name

   /**
    *  The name of the attribute that is the partition key of primary index for the TweeterFeeds DynamoDB table.
    */
   private static final String INDEX_ATTRIBUTE_NAME = "feedOwnerAlias";

   /**
    *  The name of the attribute that is set to be a sort key for the primary index of the DynamoDB table.
    */
   private static final String SORT_ATTRIBUTE_NAME = "uniqueHash";



//////// ATTRIBUTES ////////
   private String feedOwnerAlias; // primary partition key
   private String uniqueHash; //primary sort key
   private String post;
   private Long postTimestamp;
   private String postedTimeString;
   private String postUserAlias;
   private String postUserFirstName;
   private String postUserLastName;
   private String postUserImageURL;
   private List<String> postUrls;
   private  List<String> postMentions;



//////// CONSTRUCTOR ////////

   public TweeterFeeds(){};
   public TweeterFeeds(String feedOwnerAlias, String post, Long postTimestamp, String postedTimeString, String postUserAlias, String postUserFirstName, String postUserLastName, String postUserImageURL, List<String> postUrls, List<String> postMentions) {
      this.feedOwnerAlias = feedOwnerAlias;
      this.uniqueHash = makeHash(postTimestamp, postUserAlias, post);
      this.post = post;
      this.postTimestamp = postTimestamp;
      this.postedTimeString = postedTimeString;
      this.postUserAlias = postUserAlias;
      this.postUserFirstName = postUserFirstName;
      this.postUserLastName = postUserLastName;
      this.postUserImageURL = postUserImageURL;
      this.postUrls = postUrls;
      this.postMentions = postMentions;
   }


   //////// GETTERS AND SETTERS ////////
   @DynamoDbPartitionKey
   public String getFeedOwnerAlias() {
      return feedOwnerAlias;
   }

   public void setFeedOwnerAlias(String feedOwnerAlias) {
      this.feedOwnerAlias = feedOwnerAlias;
   }

   @DynamoDbSortKey
   public String getUniqueHash() {
      return uniqueHash;
   }

   public void setUniqueHash(String uniqueHash) {
      this.uniqueHash = uniqueHash;
   }

   public String getPost() {
      return post;
   }

   public void setPost(String post) {
      this.post = post;
   }

   public Long getPostTimestamp() {
      return postTimestamp;
   }

   public void setPostTimestamp(Long postTimestamp) {
      this.postTimestamp = postTimestamp;
   }

   public String getPostedTimeString() {
      return postedTimeString;
   }

   public void setPostedTimeString(String postedTimeString) {
      this.postedTimeString = postedTimeString;
   }

   public String getPostUserAlias() {
      return postUserAlias;
   }

   public void setPostUserAlias(String postUserAlias) {
      this.postUserAlias = postUserAlias;
   }

   public String getPostUserFirstName() {
      return postUserFirstName;
   }

   public void setPostUserFirstName(String postUserFirstName) {
      this.postUserFirstName = postUserFirstName;
   }

   public String getPostUserLastName() {
      return postUserLastName;
   }

   public void setPostUserLastName(String postUserLastName) {
      this.postUserLastName = postUserLastName;
   }

   public String getPostUserImageURL() {
      return postUserImageURL;
   }

   public void setPostUserImageURL(String postUserImageURL) {
      this.postUserImageURL = postUserImageURL;
   }

   public List<String> getPostUrls() {
      return postUrls;
   }

   public void setPostUrls(List<String> postUrls) {
      this.postUrls = postUrls;
   }

   public List<String> getPostMentions() {
      return postMentions;
   }

   public void setPostMentions(List<String> postMentions) {
      this.postMentions = postMentions;
   }


   //////// DynamoTweeterTable METHODS ////////
   /**
    * Gets the name of the table in DynamoDB that contains TweeterFeeds items.
    * @return the name of the table in DynamoDB that contains TweeterFeeds items
    */
   @Override
   public String tableName() {
      return TABLE_NAME;
   }

   /**
    * Gets the name of the primary index (partition key) for the TweeterFeeds DynamoDB table.
    * @return the name of the primary index for the DynamoDB table.
    */
   @Override
   public String indexName() {
      return INDEX_NAME;
   }

   /**
    * Gets the name of the attribute that is set to be the primary index key (partition key) for the TweeterFeeds DynamoDB table.
    * @return the name of the attribute that is set to be the primary index/partition key for the TweeterFeeds table.
    */
   @Override
   public String indexAttributeName() {
      return INDEX_ATTRIBUTE_NAME;
   }

   /**
    * Gets the name of the attribute that is the sort key for the primary index of the TweeterFeeds DynamoDB table.
    * @return the name of the attribute that is the sort key for the primary index of the TweeterFeeds table.
    */
   @Override
   public String sortAttributeName() {
      return SORT_ATTRIBUTE_NAME;
   }


   /**
    * Gets the name of the secondary index for the TweeterFeeds DynamoDB table. Because TweeterFeeds does not use a secondary index, this will return null.
    * @return null (TweeterFeeds does not have a secondary index)
    */
   @Override
   public String secondaryIndexName() {
      return null;
   }

   /**
    * Gets the name of the attribute that is the index key for secondary index of the TweeterFeeds DynamoDB table. Because TweeterFeeds does not use a secondary index, this will return null.
    * @return null (TweeterFeeds does not have a secondary index)
    */
   @Override
   public String secondaryIndexAttributeName() {
      return null;
   }

   /**
    * Gets the name of the attribute that is the sort key for the secondary index of the TweeterFeeds DynamoDB table. Because TweeterFeeds does not use a secondary index, this will return null.
    * @return null (TweeterFeeds does not have a secondary sort key)
    */
   @Override
   public String secondarySortAttributeName() {
      return null;
   }

//////// ADDITIONAL METHODS ////////
   public static String makeHash(Long postTimestamp, String postUserAlias, String post) {
      String joinedText =  (postTimestamp.toString() + postUserAlias + ": " + post);
      return joinedText;
   }

   public Status statusFromFeed() {
      User equivalentPostUser = new User(getPostUserFirstName(), getPostUserLastName(), getPostUserAlias(), getPostUserImageURL());
      Status equivalentPostStatus = new Status(getPost(), equivalentPostUser, getPostTimestamp().longValue(), getPostUrls(), getPostMentions());

      return equivalentPostStatus;
   }
}
