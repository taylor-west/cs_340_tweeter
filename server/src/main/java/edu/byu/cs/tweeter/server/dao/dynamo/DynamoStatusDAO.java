package edu.byu.cs.tweeter.server.dao.dynamo;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.DynamoTweeterTable;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterFeeds;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterFollows;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterStories;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.models.DataPage;
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
 * A DAO for accessing 'status' data from the database.
 */
public class DynamoStatusDAO extends DynamoDAO implements StatusDAO {
    private static final String StoriesTableName = "TweeterStories";
    private static final Class<TweeterStories> StoriesTableClass = TweeterStories.class;

    /**
     *  The name of the primary index for the TweeterStories table.
     */
    private static final String StoriesTableIndexName = "userAlias";
//    private static final String StoriesTableSortName = "timestamp";
    private static final String StoriesUserAliasAttr = "userAlias";
    private static final String StoriesTimestampAttr = "timestamp";

    private static DynamoDbTable<TweeterStories> tweeterStoriesDynamoDbTable;
    private static DynamoDbIndex<TweeterStories> tweeterStoriesDynamoDbIndex;


    private static final String FeedsTableName = "TweeterFeeds";
    private static final Class<TweeterFeeds> FeedsTableClass = TweeterFeeds.class;

    /**
     *  The name of the primary index for the TweeterFeeds table.
     */
    private static final String FeedsTableIndexName = "feedOwnerAlias";
//    private static final String FeedsTableSortName = "uniqueHash";
    private static final String FeedsOwnerAliasAttr = "feedOwnerAlias";
    private static final String FeedsUniqueHashAttr = "uniqueHash";
//    private static final String FeedsTimestampAttr = "postTimestamp";
//    private static final String FeedsPostUserAliasAttr = "postUserAlias";

    private static DynamoDbTable<TweeterFeeds> tweeterFeedsDynamoDbTable;
    private static DynamoDbIndex<TweeterFeeds> tweeterFeedsDynamoDbIndex;

    /**
     * Gets statuses from the feed of the user specified in the request. Uses information in the
     * request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param targetUser the user whose Story/statuses are to be returned
     * @param limit the number of statuses to be returned in one page
     * @param lastStatus the last status in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the Story/statuses of the given user.
     */
    @Override
    public DataPage<Status> getStory(User targetUser, int limit, Status lastStatus) {
        assert limit > 0;
        assert targetUser.getAlias() != null;

        Key key = Key.builder()
                .partitionValue(targetUser.getAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(limit);

        if( (lastStatus != null) && isNonEmptyString(lastStatus.user.getAlias()) && (lastStatus.timestamp != null) ) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(StoriesUserAliasAttr, AttributeValue.builder().s(lastStatus.user.getAlias()).build());
            startKey.put(StoriesTimestampAttr, AttributeValue.builder().n(lastStatus.timestamp.toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Status> statusPage = new DataPage<Status>();

        DynamoDbTable<TweeterStories> table = getStoriesTable();
        SdkIterable<Page<TweeterStories>> sdkIterable = table.query(request);
        PageIterable<TweeterStories> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterStories> page) -> {
                    statusPage.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> statusPage.getValues().add(entry.statusFromStory(targetUser)));
                });

        return statusPage;
    }

    /**
     * Gets statuses from the feed of the user specified in the request. Uses information in the
     * request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param userAlias the alias of the user whose feed is to be returned
     * @param limit the number of statuses to be returned in one page
     * @param lastStatus the last status in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the feed for the given user, comprised of statuses of the user's following/followees.
     */
    @Override
    public DataPage<Status> getFeed(String userAlias, int limit, Status lastStatus) {
        System.out.println("in DynamoStatusDAO.getFeed for: userAlias=" + userAlias + ", limit=" + limit + ", lastStatus=" + lastStatus);
        assert limit > 0;
        assert userAlias != null;
        assert lastStatus.getTimestamp() != null;
        assert lastStatus.getUser() != null;
        assert lastStatus.getUser().getAlias() != null;
        assert lastStatus.getPost() != null;

        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        System.out.println("  getFeed: finished building Key");

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(limit);
        System.out.println("  getFeed: finished building requestBuilder");

        if( (lastStatus != null) && isNonEmptyString(lastStatus.getUser().getAlias()) ) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FeedsOwnerAliasAttr, AttributeValue.builder().s(userAlias).build());

            String lastStatusUniqueHash = TweeterFeeds.makeHash(lastStatus.getTimestamp(),
                    lastStatus.getUser().getAlias(), lastStatus.getPost());
            startKey.put(FeedsUniqueHashAttr, AttributeValue.builder().s(lastStatusUniqueHash).build());
            System.out.println("  getFeed: finished making startKey");

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();
        System.out.println("  getFeed: finished building request");

        DataPage<Status> statusPage = new DataPage<Status>();
        System.out.println("  getFeed: finished initializing DataPage<Status>");

        System.out.println("  getFeed: attempting to get Feeds table");
        DynamoDbTable<TweeterFeeds> table = getFeedsTable();
        System.out.println("  getFeed: finished getting Feeds table");

        SdkIterable<Page<TweeterFeeds>> sdkIterable = table.query(request);
        PageIterable<TweeterFeeds> pages = PageIterable.create(sdkIterable);

        if(pages != null){
            System.out.println("  getFeed: pages=" + pages.stream().toString());
        }else{
            System.out.println("  getFeed: pages=null");
        }
        pages.stream()
                .limit(1)
                .forEach((Page<TweeterFeeds> page) -> {
                    System.out.println("  getFeed: processing page...");

                    statusPage.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(entry -> statusPage.getValues().add(entry.statusFromFeed()));
                    System.out.println("    getFeed: finished processing page");
                });

        System.out.println("  getFeed: finished all");
        return statusPage;
    }

    /**
     * Inserts a new status into the TweeterStories database which is associated with the user
     * specified in the request.
     *
     * @param status the status to post
     */
    @Override
    public void postStatus(Status status) {
        assert status != null;
        assert status.getUser() != null;
        assert status.getUser().getAlias() != null;

        DynamoDbTable<TweeterStories> table = getStoriesTable();

        TweeterStories newStory = new TweeterStories();
        newStory.setMentionedAliases(status.getMentions());
        newStory.setUrls(status.getUrls());
        newStory.setTimestamp(status.getTimestamp());
        newStory.setTimestampString(Instant.ofEpochSecond(status.getTimestamp()).toString());
        newStory.setPostText(status.getPost());
        newStory.setUserAlias(status.getUser().getAlias());

        table.putItem(newStory);
    }

    @Override
    public void postToFeed(String feedOwnerAlias, Status status) {
        assert feedOwnerAlias != null;
        assert status != null;
        assert status.getUser() != null;
        assert status.getUser().getAlias() != null;

        DynamoDbTable<TweeterFeeds> table = getFeedsTable();

        TweeterFeeds newFeed = new TweeterFeeds(feedOwnerAlias,
                status.getPost(), status.getTimestamp(),
                Instant.ofEpochSecond(status.timestamp).toString(), status.getUser().getAlias(),
                status.getUser().getFirstName(), status.getUser().getLastName(),
                status.getUser().getImageUrl(), status.getUrls(), status.getMentions());
        table.putItem(newFeed);
    }


    @Override
    public void batchPostToFeed(List<String> feedOwnerAliases, Status status) {
        System.out.println("in DynamoStatusDAO.batchPostToFeed for " + feedOwnerAliases.size() + " feeds: statusfeedOwnerAliases= [" + feedOwnerAliases.get(0) + " ... " + feedOwnerAliases.get(feedOwnerAliases.size()-1) + "]" + ", status=" + status);

        assert feedOwnerAliases != null;
        assert feedOwnerAliases.size() > 0;
        assert feedOwnerAliases.size() <= 25;
        assert status != null;
        assert status.getUser() != null;
        assert status.getUser().getAlias() != null;

        // creates list of TweeterFeed objects to insert into table
        List<TweeterFeeds> tweeterFeedsList = new ArrayList<>();
        List<TweeterFeeds> chunkList = new ArrayList<>();
//        for(String followerAlias : feedOwnerAliases){
//            TweeterFeeds newFeed = new TweeterFeeds(followerAlias,
//                    status.getPost(), status.getTimestamp(),
//                    Instant.ofEpochSecond(status.timestamp).toString(), status.getUser().getAlias(),
//                    status.getUser().getFirstName(), status.getUser().getLastName(),
//                    status.getUser().getImageUrl(), status.getUrls(), status.getMentions());
//
////            System.out.println("  adding TweeterFeeds object #" + iter++ + "to tweeterFeedsList: " + newFeed.toString());
//            tweeterFeedsList.add(newFeed);
//        }
//
//        // batch updates feed table
//        writeChunkOfTweeterFeeds(tweeterFeedsList);
//        System.out.println("exit DynamoStatusDAO.batchPostToFeed");
        for(String followerAlias : feedOwnerAliases){
            if(chunkList.size() >= 25){
                // batch updates feed table
//                System.out.println();
//                System.out.println("    writing chunk...");
                writeChunkOfTweeterFeeds(chunkList);
                tweeterFeedsList.addAll(chunkList);

                // clears chunklist to preserve the count
                chunkList.clear();
//                System.out.print("  adding TweeterFeeds objects to chunkList: ");
            }

            TweeterFeeds newFeed = new TweeterFeeds(followerAlias,
                    status.getPost(), status.getTimestamp(),
                    Instant.ofEpochSecond(status.timestamp).toString(), status.getUser().getAlias(),
                    status.getUser().getFirstName(), status.getUser().getLastName(),
                    status.getUser().getImageUrl(), status.getUrls(), status.getMentions());

            chunkList.add(newFeed);
        }

        if(chunkList.size() > 0) {
            writeChunkOfTweeterFeeds(chunkList);
            tweeterFeedsList.addAll(chunkList);
        }

        System.out.println("exit DynamoStatusDAO.batchPostToFeed (wrote " + tweeterFeedsList.size() + " feeds)");
        return;
    }


    private void writeChunkOfTweeterFeeds(List<TweeterFeeds> tweeterFeedsList) {
        System.out.println("      in DynamoStatusDAO.writeChunkOfTweeterFeeds for " +  tweeterFeedsList.size() + " TweeterFeeds: tweeterFeedsList=[" + tweeterFeedsList.get(0).getFeedOwnerAlias() + " ... " + tweeterFeedsList.get(tweeterFeedsList.size()-1).getFeedOwnerAlias() + "]");

        DynamoDbTable<TweeterFeeds> table = getFeedsTable();

        if(tweeterFeedsList.size() > 25){
            throw new RuntimeException("ERROR: Too many TweeterFollows for DynamoStatusDAO.writeChunkOfTweeterFeeds to write (" + tweeterFeedsList.size() + " feeds attempted, from [" + tweeterFeedsList.get(0).getFeedOwnerAlias() + " ... " + tweeterFeedsList.get(tweeterFeedsList.size()-1).getFeedOwnerAlias() + "])");
        }


        WriteBatch.Builder<TweeterFeeds> writeBuilder = WriteBatch.builder(FeedsTableClass).mappedTableResource(table);

        for(TweeterFeeds feed : tweeterFeedsList){
//            System.out.println("  adding TweeterFeeds object #" + iter++ + " to writeBuilder: " + feed.toString());
            writeBuilder.addPutItem(builder -> builder.item(feed));
        }

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
//            System.out.println("  Attempting to batch write TweeterFeeds objects...");
            BatchWriteResult result = getEnhancedDynamoClient().batchWriteItem(batchWriteItemEnhancedRequest);

            int numObjectWritten = (tweeterFeedsList.size() - result.unprocessedPutItemsForTable(table).size());
            System.out.println("          successfully wrote " + numObjectWritten + " feeds!");

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
//                System.out.println("    unprocessed items present, running again...");
                writeChunkOfTweeterFeeds(result.unprocessedPutItemsForTable(table));
            }
            System.out.println("          finished write.");
        } catch (DynamoDbException e) {
            throw new RuntimeException("[Server Error] Problem occurred while in writeChunkOfTweeterFeeds: " + e.getMessage());
        }
        System.out.println("        exit DynamoStatusDAO.writeChunkOfTweeterFeeds");
    }

    /**
     * Gets a singleton instance of the TweeterStories DynamoDbIndex.
     * @return an instance of the TweeterStories DynamoDbIndex.
     */
    private DynamoDbIndex<TweeterStories> getStoriesIndex(){
        if(tweeterStoriesDynamoDbIndex == null){
            tweeterStoriesDynamoDbIndex = getEnhancedDynamoClient().table(StoriesTableName, TableSchema.fromBean(StoriesTableClass)).index(StoriesTableIndexName);
        }

        return tweeterStoriesDynamoDbIndex;
    }

    /**
     * Gets a singleton instance of the TweeterStories DynamoDbTable table.
     * @return an instance of the DynamoDbTable
     */
    private DynamoDbTable<TweeterStories> getStoriesTable(){
        if(tweeterStoriesDynamoDbTable == null){
            tweeterStoriesDynamoDbTable = getEnhancedDynamoClient().table(StoriesTableName, TableSchema.fromBean(StoriesTableClass));
        }

        return tweeterStoriesDynamoDbTable;
    }

    /**
     * Gets a singleton instance of the TweeterFeeds DynamoDbIndex.
     * @return an instance of the TweeterFeeds DynamoDbIndex.
     */
    private DynamoDbIndex<TweeterFeeds> getFeedsIndex(){
        System.out.println("in DynamoStatusDAO.getFeedsIndex");
        if(tweeterStoriesDynamoDbIndex == null){
            tweeterFeedsDynamoDbIndex = getEnhancedDynamoClient().table(FeedsTableName, TableSchema.fromBean(FeedsTableClass)).index(FeedsTableIndexName);
        }

        return tweeterFeedsDynamoDbIndex;
    }

    /**
     * Gets a singleton instance of the TweeterFeeds DynamoDbTable table.
     * @return an instance of the DynamoDbTable
     */
    private DynamoDbTable<TweeterFeeds> getFeedsTable(){
        if(tweeterFeedsDynamoDbTable == null){
            tweeterFeedsDynamoDbTable = getEnhancedDynamoClient().table(FeedsTableName, TableSchema.fromBean(FeedsTableClass));
        }

        return tweeterFeedsDynamoDbTable;
    }
//    /**
//     * Determines the index for the first status in the specified 'allStatuses' list that should
//     * be returned in the current request. This will be the index of the next status after the
//     * specified 'lastStatus'.
//     *
//     * @param lastStatus the last status that was returned in the previous
//     *                          request or null if there was no previous request.
//     * @param allStatuses the generated list of statuses from which we are returning paged results.
//     * @return the index of the first status to be returned.
//     */
//    private int getStartingIndex(Status lastStatus, List<Status> allStatuses) {
//
//        int statusesIndex = 0;
//
//        if(lastStatus != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allStatuses.size(); i++) {
//                if(lastStatus.equals(allStatuses.get(i))) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    statusesIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return statusesIndex;
//    }
//
//    /**
//     * Adds a new status to the database which is associated with the given user.. This is written
//     * as a separate method to allow for mocking.
//     *
//     * @return the statuses.
//     */
//    void postDummyStatus(String userAlias, Status status) {
//        return;
//    }
//
//    /**
//     * Returns a dummy Feed via a list of dummy status data. This is written as a separate method
//     * to allow mocking of the statuses.
//     *
//     * @return the statuses.
//     */
//    List<Status> getDummyFeed() {
//        return getFakeData().getFakeStatuses();
//    }
//
//    /**
//     * Returns a dummy Story via a list of dummy status data. This is written as a separate method
//     * to allow mocking of the statuses.
//     *
//     * @return the statuses.
//     */
//    List<Status> getDummyStory() {
//        return getFakeData().getFakeStatuses();
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

}
