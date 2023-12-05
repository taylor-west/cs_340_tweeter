package edu.byu.cs.tweeter.server.dao.dynamo;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterFollows;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.models.DataPage;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowDAO extends DynamoDAO<TweeterFollows> implements FollowDAO {
    private static final String FollowsTableName = "TweeterFollows";
    private static final Class<TweeterFollows> FollowsTableClass = TweeterFollows.class;

    /**
     *  The name of the primary index for the TweeterFollows table.
     */
    private static final String FollowsTableIndexName = "followerAlias";
    private static final String FollowsTableSecondaryIndexName = "followeeAlias-followerAlias-index";
    private static final String FollowerHandleAttr = "followerAlias";
    private static final String FolloweeHandleAttr = "followeeAlias";

    private static DynamoDbTable<TweeterFollows> tweeterFollowsDynamoDbTable;


    /**
     * Adds a new association between the given follower and followee. The current implementation uses
     * generated data and doesn't actually access a database.
     *
     * @param follower the User who is following the followee
     * @param followee the User who is being followed by the follower
     */
    @Override
    public void follow(User follower, User followee) {
        assert follower.getAlias() != null;
        assert followee.getAlias() != null;
        assert followee.getFirstName() != null;
        assert followee.getLastName() != null;
        assert followee.getImageUrl() != null;
        //insert(FOLLOWS_TABLE_NAME, FOLLOWS_TABLE_CLASS, newFollow);

        try{
            DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();

            TweeterFollows newFollow = new TweeterFollows();
            newFollow.setFollowerAlias(follower.getAlias());
            newFollow.setFollowerFirstName(follower.getFirstName());
            newFollow.setFollowerLastName(follower.getLastName());
            newFollow.setFollowerImageURL(follower.getImageUrl());
            newFollow.setFolloweeAlias(followee.getAlias());
            newFollow.setFolloweeFirstName(followee.getFirstName());
            newFollow.setFolloweeLastName(followee.getLastName());
            newFollow.setFolloweeImageURL(followee.getImageUrl());

            table.putItem(newFollow);
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while inserting Follow into database (partitionValue=" + follower.getAlias() + ", sortValue=" + followee.getAlias() + "): " + e.getMessage());
        }
    }

    /**
     * Removes an existing  association between the given follower and followee. The current
     * implementation uses generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the follower (User who was following the followee)
     * @param followeeAlias the alias of the followee (User who was being followed by the follower)
     */
    @Override
    public void unfollow(String followerAlias, String followeeAlias) {
        assert followerAlias != null;
        assert followeeAlias != null;


        try{
            DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();
            Key key = Key.builder()
                    .partitionValue(followerAlias).sortValue(followeeAlias)
                    .build();

            System.out.println("attempting to delete follow record with partitionValue: " + followerAlias + ", sortValue: " + followeeAlias);
            TweeterFollows follow = table.getItem(key);
            if(follow != null){
                System.out.println("follow found: " + follow.toString());
                TweeterFollows deletedFollows = table.deleteItem(key);

                if(deletedFollows == null){
                   System.out.println("no record was deleted");
                   throw new Exception("no record was deleted");
                }else{
                    System.out.println("record deleted successfully: " + deletedFollows.toString());
                }
            }else{
                System.out.println("follow record not found");
                throw new Exception("record not found");
            }
            // deletes TweeterAuthToken from the database

        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while deleting Follow from database (partitionValue=" + followerAlias + ", sortValue=" + followeeAlias + "): " + e.getMessage());
        }
    }

    /**
     * Checks the database to see if the follower is currently following the followee. The current \
     * implementation uses dummy data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the follower (User who should be following the followee)
     * @param followeeAlias the alias of the followee (User who should be followed by the follower)
     *
     * @return a boolean indicating whether or not the follower is currently following the followee
     */
    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        //TweeterFollows dbFollows = find(FOLLOW_TABLE_NAME, FOLLOW_TABLE_CLASS, followerAlias, followeeAlias);
        assert followerAlias != null;
        assert followeeAlias != null;

        boolean followExists = false;
        TweeterFollows dbFollows = null;
        try{
            DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();
            Key key = Key.builder()
                    .partitionValue(followerAlias).sortValue(followeeAlias)
                    .build();


            // get TweeterAuthToken from database
            dbFollows = table.getItem(key);

            if(dbFollows != null){
                followExists = true;
            }
        }catch (Exception e){
            throw new RuntimeException("[DAO Error] Error thrown while getting Follows for isFollower (partitionValue=" + followerAlias + ", sortValue=" + followeeAlias + "): " + e.getMessage());
        }

        return followExists;
    }

//    /**
//     * Gets the count of users from the database that the user specified is following. The
//     * current implementation uses generated data and doesn't actually access a database.
//     *
//     * @param followerAlias the alias of the User whose count of how many following is desired.
//     * @return said count.
//     */
//    @Override
//    public Integer getFollowingCount(String followerAlias) {
//        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert followerAlias != null;
//        return getDummyFollowees().size();
//    }
//
//    /**
//     * Gets the count of users from the database that are following (are followers of) the user
//     * specified. The current implementation uses generated data and doesn't actually access a
//     * database.
//     *
//     * @param followeeAlias the alias of the User whose count of how many followers is desired.
//     * @return said count.
//     */
//    @Override
//    public Integer getFollowerCount(String followeeAlias) {
//        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert followeeAlias != null;
//        return getDummyFollowers().size();
//    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the user whose followees are to be returned
     * @param limit the number of followees to be returned in one page
     * @param lastFolloweeAlias the alias of the last followee in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followees.
     */
    @Override
    public DataPage<User> getFollowing(String followerAlias, int limit, String lastFolloweeAlias) {
        assert limit > 0;
        assert followerAlias != null;

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if( (lastFolloweeAlias != null) && isNonEmptyString(lastFolloweeAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<User>();

        DynamoDbTable<TweeterFollows> table = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass));
        SdkIterable<Page<TweeterFollows>> sdkIterable = table.query(request);
        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterFollows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> result.getValues().add(entry.userFromFollowee()));
                });

        return result;
    }

    /**
     * Gets the users from the database that are following the user specified in the request. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followeeAlias the alias of the user whose followers are to be returned
     * @param limit the number of followers to be returned in one page
     * @param lastFollowerAlias the alias of the last follower in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followers.
     */
    @Override
    public DataPage<User> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        assert limit > 0;
        assert followeeAlias != null;

        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        System.out.println("in getFollowers for partitionValue: " + followeeAlias);

        if( (lastFollowerAlias != null) && isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<User>();

        DynamoDbIndex<TweeterFollows> index = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);;
        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterFollows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> result.getValues().add(entry.userFromFollower()));
                });

        System.out.println("returning getFollowers: " + result.toString());
        return result;
    }

    @Override
    public List<String> getFollowersAliases(String followeeAlias){
        // List<String> followersAliases = find(FOLLOWS_TABLE_NAME, FOLLOWS_TABLE_CLASS, followeeAlias, null);
        assert followeeAlias != null;

        DynamoDbIndex<TweeterFollows> index = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);
        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .build();

        List<String> followerAliases = new ArrayList<String>();

        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .forEach((Page<TweeterFollows> page) -> {
                    page.items().forEach(entry -> followerAliases.add(entry.getFollowerAlias()));
                });

        return followerAliases;
    }

    /**
     * Gets a singleton instance of the TweeterFollows DynamoDbTable table.
     * @return an instance of the TweeterFollows DynamoDbTable table.
     */
    private DynamoDbTable<TweeterFollows> getTweeterFollowsTable(){
        if(tweeterFollowsDynamoDbTable == null){
            tweeterFollowsDynamoDbTable = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass));
        }

        return tweeterFollowsDynamoDbTable;
    }
}
