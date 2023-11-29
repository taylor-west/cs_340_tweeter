package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.observers.PostStatusObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
//    public static final String URL_PATH = "/status";
    public static final String FEED_URL_PATH = "/status/feed";
    public static final String POST_STATUS_URL_PATH = "/status/post";
    public static final String STORY_URL_PATH = "/status/story";

    /**
     * Creates an instance.
     */
    public StatusService() {}

    /**
     * Requests the story for the user specified in the request.
     * Limits the number of statuses returned and returns the next set of
     * statuses after any that were returned in a previous request.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param currUser the user who is currently logged in
     * @param targetUser the user for whom the feed is being retrieved.
     * @param pageSize the maximum number of statuses to return.
     * @param lastStatus the last status returned in the previous request (can be null).
     */
    public void getStory(AuthToken authToken, User currUser, User targetUser, int pageSize, Status lastStatus,
                         PagedObserver<Status> observer) {
        GetStoryTask getStoryTask = getGetStoryTask(authToken, currUser, targetUser, pageSize, lastStatus, observer);
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public static String getStoryUrl(String userAlias){
        return STORY_URL_PATH + "/" + userAlias;
    }

    public void postStatus(AuthToken currUserAuthToken, User currUser, User targetUser, Status newStatus,
                           PostStatusObserver observer) {
        PostStatusTask postStatusTask = getPostStatusTask(currUserAuthToken, currUser, targetUser, newStatus, observer);
        BackgroundTaskUtils.runTask(postStatusTask);
    }

    public static String getPostStatusUrlPath(String userAlias){
        return POST_STATUS_URL_PATH + "/" + userAlias;
    }


    /**
     * Requests the feed for the user specified in the request.
     * Limits the number of statuses returned and returns the next set of
     * statuses after any that were returned in a previous request.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param currUser the user who is currently logged in
     * @param targetUser the user for whom the feed is being retrieved.
     * @param pageSize the maximum number of statuses to return.
     * @param lastStatus the last status returned in the previous request (can be null).
     */
    public void getFeed(AuthToken authToken, User currUser, User targetUser, int pageSize, Status lastStatus,
                        PagedObserver<Status> observer) {
        GetFeedTask getFeedTask = getGetFeedTask(authToken, currUser, targetUser, pageSize, lastStatus, observer);
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public static String getGetFeedUrlPath(String userAlias){
        return FEED_URL_PATH + "/" + userAlias;
    }


    /**
     * Returns an instance of {@link GetFeedTask}. Allows mocking of the
     * GetFeedTask class for testing purposes. All usages of GetFeedTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFeedTask getGetFeedTask(AuthToken authToken, User currUser, User targetUser, int pageSize,
                                      Status lastStatus, PagedObserver<Status> observer){
        return new GetFeedTask(authToken, currUser, targetUser, pageSize, lastStatus,
                new PagedHandler<Status>(observer, "get feed",
                        GetFeedTask.STATUSES_KEY));
    }

    /**
     * Returns an instance of {@link GetStoryTask}. Allows mocking of the
     * GetStoryTask class for testing purposes. All usages of GetStoryTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetStoryTask getGetStoryTask(AuthToken authToken, User currUser, User targetUser, int pageSize,
                                        Status lastStatus, PagedObserver<Status> observer){
        return new GetStoryTask(authToken, currUser, targetUser, pageSize, lastStatus,
                new PagedHandler<Status>(observer, "get story",
                        GetStoryTask.STATUSES_KEY));
    }

    /**
     * Returns an instance of {@link PostStatusTask}. Allows mocking of the
     * PostStatusTask class for testing purposes. All usages of PostStatusTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public PostStatusTask getPostStatusTask(AuthToken authToken, User currUser, User targetUser,
                                            Status newStatus, PostStatusObserver observer) {
        return new PostStatusTask(authToken, currUser, targetUser, newStatus, new PostStatusHandler(observer));
    }

}


