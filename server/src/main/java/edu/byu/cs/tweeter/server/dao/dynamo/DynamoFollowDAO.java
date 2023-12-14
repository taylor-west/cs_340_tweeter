package edu.byu.cs.tweeter.server.dao.dynamo;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterFollows;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterUsers;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.models.DataPage;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

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
     * Removes a up to 25 User entries from the database at a time
     *
     * @param followPairList A list of aliases belonging to the users that should be deleted
     */
    public void batchUnfollow(List<Pair<String, String>> followPairList) {
        assert followPairList != null;
        System.out.println("in DynamoFollowDAO.batchUnfollow with followPairList: " + followPairList.toString());

        DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();

//        WriteBatch.Builder<TweeterFollows> writeBuilder = WriteBatch.builder(FollowsTableClass).mappedTableResource(table);

        int iter = 0;
        List<Pair<String, String>> deletedFollowsList = new ArrayList<>();
        List<Key> keyChunkList = new ArrayList<>();

        for(Pair<String, String> followPair : followPairList){
            System.out.println("  adding follow-pair #" + iter++ + " to chunkList");

            Key key = Key.builder()
                    .partitionValue(followPair.getFirst()).sortValue(followPair.getSecond())
                    .build();
            keyChunkList.add(key);

            if(keyChunkList.size() >= 25){

                // batch delete users from table
                System.out.println("    writing chunk to list...");
                deleteChunkOfTweeterFollows(keyChunkList);

                // clears chunklist to preserve the count
                keyChunkList.clear();
            }

            deletedFollowsList.add(followPair);
        }

        if(keyChunkList.size() > 0) {
            deleteChunkOfTweeterFollows(keyChunkList);
        }

        System.out.println("  deleted " + deletedFollowsList.size() + " follows");
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

        System.out.println("in getFollowers: limit=" + limit +  ", partitionValue=" + followeeAlias + ", lastFollowerAlias=" + lastFollowerAlias);

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

        System.out.println("  returning getFollowers with " + result.getValues().size() + " followers: [" + result.getValues().get(0).getAlias() + " ... " + result.getValues().get(result.getValues().size() -1).getAlias() + "]");
        return result;
    }

    /**
     * Returns a list of follower aliases, grouped into pages.
     * @param followeeAlias
     * @return
     */
    @Override
    public DataPage<String> getFollowersAliases(String followeeAlias, int limit, String lastFollowerAlias) {
//        assert followeeAlias != null;
//
//        DynamoDbIndex<TweeterFollows> index = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);
//        Key key = Key.builder()
//                .partitionValue(followeeAlias)
//                .build();
//
//        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .build();
//
//        List< String > followerAliases = new ArrayList<>();
//
//        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
//        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
//        pages.stream()
//                .forEach((Page<TweeterFollows> page) -> {
//                    page.items().forEach(entry -> followerAliases.add(entry.getFollowerAlias()));
//                });
//
//        return followerAliases;

        assert limit > 0;
        assert followeeAlias != null;

        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        System.out.println("in getFollowersAliases: limit=" + limit +  ", partitionValue=" + followeeAlias + ", lastFollowerAlias=" + lastFollowerAlias);

        if( (lastFollowerAlias != null) && isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<String> result = new DataPage<>();

        DynamoDbIndex<TweeterFollows> index = getEnhancedDynamoClient().table(FollowsTableName, TableSchema.fromBean(FollowsTableClass)).index(FollowsTableSecondaryIndexName);;
        SdkIterable<Page<TweeterFollows>> sdkIterable = index.query(request);
        PageIterable<TweeterFollows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterFollows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> result.getValues().add(entry.userFromFollower().getAlias()));
                });

        System.out.println("  returning getFollowersAliases with " + result.getValues().size() + " follower aliases: [" + result.getValues().get(0) + " ... " + result.getValues().get(result.getValues().size() -1) + "]");
        return result;
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


    /**
     * Adds up to 25 "follow" relationships to the database at a time
     * @param follows a list of TweeterFollows objects to add to the database
     */
    public void batchFollow(List<TweeterFollows> follows) {
        assert follows != null;
        System.out.println("in DynamoFollowDAO.batchFollow for " + follows.size() + "TweeterFollows: [" + follows.get(0).getFollowerAlias() + " ... " + follows.get(follows.size()-1).getFollowerAlias() + "]");

        // creates list of TweeterFeed objects to insert into table
        // adds the TweeterFeed objects to a request
        List<TweeterFollows> tweeterFollowsList = new ArrayList<>();
        int iter = 0;
        List<TweeterFollows> chunkList = new ArrayList<>();

//        System.out.print("  adding TweeterFollows objects to chunkList: ");
        for(TweeterFollows follow : follows){
//            System.out.print( follow.getFollowerAlias() + ", " );
            if(chunkList.size() >= 25){
                // batch updates feed table
//                System.out.println();
//                System.out.println("    writing chunk...");
                writeChunkOfTweeterFollows(chunkList);
                tweeterFollowsList.addAll(chunkList);

                // clears chunklist to preserve the count
                chunkList.clear();
//                System.out.print("  adding TweeterFollows objects to chunkList: ");
            }

            chunkList.add(follow);
        }

        if(chunkList.size() > 0) {
            writeChunkOfTweeterFollows(chunkList);
            tweeterFollowsList.addAll(chunkList);
        }

        System.out.println("DynamoFollowDAO.batchFollow wrote " + tweeterFollowsList.size() + " follows");
        return;
    }


    private void writeChunkOfTweeterFollows(List<TweeterFollows> tweeterFollowsList) {
        System.out.println("      in DynamoFollowDAO.writeChunkOfTweeterFollows for " + tweeterFollowsList.size()  + " TweeterFollows: [" + tweeterFollowsList.get(0).getFollowerAlias() + " ... " + tweeterFollowsList.get(tweeterFollowsList.size()-1).getFollowerAlias() + "]");

        DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();

        if(tweeterFollowsList.size() > 25){
            throw new RuntimeException("ERROR: Too many TweeterFollows for DynamoFollowDAO.writeChunkOfTweeterFollows to write (" + tweeterFollowsList.size() + " follows attempted, from [" + tweeterFollowsList.get(0).getFollowerAlias() + " ... " + tweeterFollowsList.get(tweeterFollowsList.size()-1).getFollowerAlias() + "])");
        }


        WriteBatch.Builder<TweeterFollows> writeBuilder = WriteBatch.builder(FollowsTableClass).mappedTableResource(table);

        for(TweeterFollows follow : tweeterFollowsList){
            writeBuilder.addPutItem(builder -> builder.item(follow));
        }
//        System.out.print("        added " + tweeterFollowsList.size() + " keys to writeBuilder") ;
//        System.out.println();

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
//            System.out.println("        Attempting to batch write TweeterFollows objects...");
            BatchWriteResult result = getEnhancedDynamoClient().batchWriteItem(batchWriteItemEnhancedRequest);

            int numObjectWritten = (tweeterFollowsList.size() - result.unprocessedPutItemsForTable(table).size());
            System.out.println("          successfully wrote " + numObjectWritten + " objects!");

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
//                System.out.println("           unprocessed items present, running again...");
                writeChunkOfTweeterFollows(result.unprocessedPutItemsForTable(table));
            }
            System.out.println("          finished write.");
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ERROR: problem while bulk adding Follows: " + e.getMessage());
        }
        System.out.println("        exit DynamoFollowsDAO.writeChunkOfTweeterFollows");
    }

    private void deleteChunkOfTweeterFollows(List<Key> keyList) {
        System.out.println("in DynamoFollowDAO.deleteChunkOfTweeterFollows");

        DynamoDbTable<TweeterFollows> table = getTweeterFollowsTable();

        if(keyList.size() > 25){
            throw new RuntimeException("ERROR: Too many follows to delete (attempted " + keyList.size() + " keys)");
        }


        WriteBatch.Builder<TweeterFollows> writeBuilder = WriteBatch.builder(FollowsTableClass).mappedTableResource(table);

        int iter = 0;
        System.out.print("  adding keys to writeBuilder: # ");
        for(Key key : keyList){
            System.out.print(iter++ + ", ");
            writeBuilder.addDeleteItem(key);
        }

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            System.out.println("  Attempting to batch delete " + keyList.size() + " follows...");
            BatchWriteResult result = getEnhancedDynamoClient().batchWriteItem(batchWriteItemEnhancedRequest);
            System.out.println("    result=" + result.toString());

            int numObjectWritten = (keyList.size() - result.unprocessedPutItemsForTable(table).size());
            System.out.println("    successfully deleted " + numObjectWritten + " objects!");

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                System.out.println("    unprocessed items present, running again...");
                deleteChunkOfTweeterFollows(result.unprocessedDeleteItemsForTable(table));
            }
            System.out.println("    finished deleted.");
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("  exit DynamoFollowDAO.deleteChunkOfTweeterFollows");
    }

}
