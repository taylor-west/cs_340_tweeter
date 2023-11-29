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
public class DynamoFollowDAO extends DynamoDAO implements FollowDAO {
    private static final String FollowsTableName = "TweeterFollows";
    private static final Class<TweeterFollows> FollowsTableClass = TweeterFollows.class;

    /**
     *  The name of the primary index for the TweeterFollows table.
     */
    private static final String FollowsTableIndexName = "followerAlias";
    private static final String FollowsTableSecondaryIndexName = "followeeAlias";
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

            TweeterFollows newFollow = new TweeterFollows(follower.getAlias(), follower.getFirstName(), follower.getLastName(), follower.getImageUrl(),followee.getAlias(), followee.getFirstName(), followee.getLastName(), followee.getImageUrl());
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
        //delete(AUTHTOKEN_TABLE_NAME, AUTHTOKEN_TABLE_CLASS, authToken.partitionValue(), null);
        assert followerAlias != null;
        assert followeeAlias != null;


        try{
            DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();
            Key key = Key.builder()
                    .partitionValue(followerAlias).sortValue(followeeAlias)
                    .build();

            // deletes TweeterAuthToken from the database
            TweeterFollows deletedFollows = table.deleteItem(key);
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

//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(limit);
//        System.out.println("FollowDAO found " + allFollowees.size() + " total followees for user: " + followerAlias); // TODO: remove
//
//        boolean hasMorePages = false;
//
//        if(limit > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFollowingStartingIndex(lastFolloweeAlias, allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//
//        return new Pair<>(responseFollowees, hasMorePages);

        DynamoDbIndex<TweeterFollows> index = enhancedClient.table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableIndexName);
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastFolloweeAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<User>();

        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
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
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert followeeAlias != null;

//        List<User> allFollowers = getDummyFollowers();
//        List<User> responseFollowers = new ArrayList<>(limit);
//        System.out.println("FollowDAO found " + allFollowers.size() + " total followers for user: " + followeeAlias); // TODO: remove
//
//        boolean hasMorePages = false;
//
//        if(limit > 0) {
//            if (allFollowers != null) {
//                int followeesIndex = getFollowersStartingIndex(lastFollowerAlias, allFollowers);
//
//                for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
//                    responseFollowers.add(allFollowers.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowers.size();
//            }
//        }
//
//        return new Pair<>(responseFollowers, hasMorePages);

        DynamoDbIndex<TweeterFollows> index = enhancedClient.table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);
        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<User>();

        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterFollows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> result.getValues().add(entry.userFromFollower()));
                });

        return result;
    }

    @Override
    public List<String> getFollowersAliases(String followeeAlias){
        // List<String> followersAliases = find(FOLLOWS_TABLE_NAME, FOLLOWS_TABLE_CLASS, followeeAlias, null);
        assert followeeAlias != null;

        DynamoDbIndex<TweeterFollows> index = enhancedClient.table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);
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

//    /**
//     * Determines the index for the first followee in the specified 'allFollowees' list that should
//     * be returned in the current request. This will be the index of the next followee after the
//     * specified 'lastFollowee'.
//     *
//     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
//     *                          request or null if there was no previous request.
//     * @param allFollowees the generated list of followees from which we are returning paged results.
//     * @return the index of the first followee to be returned.
//     */
//    private int getFollowingStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {
//
//        int followeesIndex = 0;
//
//        if(lastFolloweeAlias != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowees.size(); i++) {
//                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followeesIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return followeesIndex;
//    }
//
//    /**
//     * Determines the index for the first follower in the specified 'allFollowers' list that should
//     * be returned in the current request. This will be the index of the next follower after the
//     * specified 'lastFollower'.
//     *
//     * @param lastFollowerAlias the alias of the last follower that was returned in the previous
//     *                          request or null if there was no previous request.
//     * @param allFollowers the generated list of followers from which we are returning paged results.
//     * @return the index of the first follower to be returned.
//     */
//    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {
//
//        int followersIndex = 0;
//
//        if(lastFollowerAlias != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowers.size(); i++) {
//                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followersIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return followersIndex;
//    }

//    /**
//     * Returns a boolean of whether or not the follower is following the followee. This is written
//     * as a separate method to allow mocking.
//     *
//     * @return isFollower.
//     */
//    Boolean getDummyIsFollower(String followerAlias, String followeeAlias) {
//        boolean isFollower = new Random().nextInt() > 0;
//        return isFollower;
//    }
//
//    /**
//     * Returns the list of dummy followee data. This is written as a separate method to allow
//     * mocking of the followees.
//     *
//     * @return the followees.
//     */
//    List<User> getDummyFollowees() {
//        return getFakeData().getFakeUsers();
//    }
//
//    /**
//     * Returns the list of dummy follower data. This is written as a separate method to allow
//     * mocking of the followers.
//     *
//     * @return the followers.
//     */
//    List<User> getDummyFollowers() {
//        return getFakeData().getFakeUsers();
//    }
//
//    /**
//     * Returns the {@link FakeData} object used to generate dummy followees.
//     * This is written as a separate method to allow mocking of the {@link FakeData}.
//     *
//     * @return a {@link FakeData} instance.
//     */
//    FakeData getFakeData() {
//        return FakeData.getInstance();
//    }

    /**
     * Gets a singleton instance of the TweeterFollows DynamoDbTable table.
     * @return an instance of the TweeterFollows DynamoDbTable table.
     */
    private DynamoDbTable<TweeterFollows> getTweeterFollowsTable(){
        if(tweeterFollowsDynamoDbTable == null){
            tweeterFollowsDynamoDbTable = enhancedClient.table(FollowsTableName, TableSchema.fromBean(FollowsTableClass));
        }

        return tweeterFollowsDynamoDbTable;
    }
}
