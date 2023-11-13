package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.presenter.observers.FollowObserver;
import edu.byu.cs.tweeter.client.presenter.observers.IsFollowerObserver;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UnfollowObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

//    public static final String URL_PATH = "/getfollowing";
    public static final String USER_URL_PATH = "/user";
    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";

    public static final String FOLLOWERS_URL_PATH = "/followers";
    public static final String FOLLOWING_URL_PATH = "/following";
    public static final String COUNT_URL_PATH = "/count";
    public static final String IS_FOLLOWER_URL_PATH = "/isfollower";

    /**
     * Creates an instance.
     */
    public FollowService() {}

    /**
     * Requests the users that the user specified in the request is following.
     * Limits the number of followees returned and returns the next set of
     * followees after any that were returned in a previous request.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param targetUser the user for whom followees are being retrieved.
     * @param pageSize the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    public void getFollowing(AuthToken authToken, User targetUser, int pageSize, User lastFollowee, PagedObserver<User> observer) {
        GetFollowingTask getFollowingTask = getGetFollowingTask(authToken, targetUser, pageSize, lastFollowee, observer);
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    public static String getFollowingUrlPath(String userAlias){
        return USER_URL_PATH+ "/" + userAlias + FOLLOWING_URL_PATH;
    }


    /**
     * Requests the number of users that the user specified in the request is following.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param targetUser the user for whom followees are being retrieved.
     */
    public void getFollowingCount(AuthToken authToken, User targetUser, UpdateFolloweesAndFollowersObserver observer) {
        GetFollowingCountTask getFollowingCountTask = getGetFollowingCountTask(authToken, targetUser, observer);
        BackgroundTaskUtils.runTask(getFollowingCountTask);
    }

    public static String getFollowingCountUrlPath(String userAlias){
        return USER_URL_PATH+ "/" + userAlias + FOLLOWING_URL_PATH + COUNT_URL_PATH;
    }

    public void getFollowers(AuthToken authToken, User targetUser, int pageSize, User lastFollower, PagedObserver<User> observer) {
        GetFollowersTask getFollowersTask = getGetFollowersTask(authToken, targetUser, pageSize, lastFollower, observer);
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public static String getFollowersUrlPath(String userAlias){
        return USER_URL_PATH + "/" + userAlias + FOLLOWERS_URL_PATH;
    }

    public void getFollowersCount(AuthToken authToken, User targetUser, UpdateFolloweesAndFollowersObserver observer) {
        GetFollowersCountTask getFollowersCountTask = getGetFollowersCountTask(authToken, targetUser, observer);
        BackgroundTaskUtils.runTask(getFollowersCountTask);
    }

    public static String getFollowersCountUrlPath(String userAlias){
        return USER_URL_PATH + "/" + userAlias + FOLLOWERS_URL_PATH + COUNT_URL_PATH;
    }

    public void isFollower(AuthToken currUserAuthToken, User currentUser, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = getIsFollowerTask(currUserAuthToken, currentUser, selectedUser, observer);
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public static String getIsFollowerUrlPath(String followerAlias, String followeeAlias){
        return USER_URL_PATH + "/" + followerAlias + IS_FOLLOWER_URL_PATH + "/" + followeeAlias;
    }

    public void follow(AuthToken currUserAuthToken, User currentUser, User selectedUser, FollowObserver observer) {
        FollowTask followTask = getFollowTask(currUserAuthToken, currentUser, selectedUser, observer);
        BackgroundTaskUtils.runTask(followTask);
        observer.startingFollow(selectedUser);
    }

    public static String getFollowUrlPath(String followerAlias, String followeeAlias){
        return USER_URL_PATH + "/" + followerAlias + FOLLOW_URL_PATH + "/" + followeeAlias;
    }


    public void unfollow(AuthToken currUserAuthToken, User currentUser, User selectedUser, UnfollowObserver observer) {
        UnfollowTask unfollowTask = getUnfollowTask(currUserAuthToken, currentUser, selectedUser, observer);
        BackgroundTaskUtils.runTask(unfollowTask);
        observer.startingUnfollow(selectedUser);
    }

    public static String getUnfollowUrlPath(String followerAlias, String followeeAlias) {
        return USER_URL_PATH + "/" + followerAlias + UNFOLLOW_URL_PATH + "/" + followeeAlias;
    }


    /**
     * Returns an instance of {@link GetFollowingTask}. Allows mocking of the
     * GetFollowingTask class for testing purposes. All usages of GetFollowingTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowingTask getGetFollowingTask(AuthToken authToken, User targetUser, int pageSize, User lastFollowee, PagedObserver<User> observer) {
//        return new GetFollowingTask(this, authToken, targetUser, pageSize, lastFollowee, new GetFollowingTaskHandler(observer));/
        return new GetFollowingTask(authToken, targetUser, pageSize, lastFollowee, new PagedHandler<User>(observer, "get following", GetFollowingTask.FOLLOWEES_KEY));
    }

    /**
     * Returns an instance of {@link GetFollowingCountTask}. Allows mocking of the
     * GetFollowingCountTask class for testing purposes. All usages of GetFollowingCountTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowingCountTask getGetFollowingCountTask(AuthToken authToken, User targetUser, UpdateFolloweesAndFollowersObserver observer) {
        return new GetFollowingCountTask(authToken, targetUser, new GetFollowingCountHandler(observer, "get following count"));
    }


    /**
     * Returns an instance of {@link GetFollowersTask}. Allows mocking of the
     * GetFollowersTask class for testing purposes. All usages of GetFollowersTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowersTask getGetFollowersTask(AuthToken authToken, User targetUser, int pageSize, User lastFollower, PagedObserver<User> observer) {
        return new GetFollowersTask(authToken, targetUser, pageSize, lastFollower, new PagedHandler<User>(observer, "get followers", GetFollowersTask.FOLLOWERS_KEY));
    }

    /**
     * Returns an instance of {@link GetFollowersCountTask}. Allows mocking of the
     * GetFollowersCountTask class for testing purposes. All usages of GetFollowersCountTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowersCountTask getGetFollowersCountTask(AuthToken authToken, User targetUser, UpdateFolloweesAndFollowersObserver observer) {
        return new GetFollowersCountTask(authToken, targetUser, new GetFollowersCountHandler(observer, "get followers count"));
    }

    /**
     * Returns an instance of {@link IsFollowerTask}. Allows mocking of the
     * IsFollowerTask class for testing purposes. All usages of IsFollowerTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public IsFollowerTask getIsFollowerTask(AuthToken currUserAuthToken,
                                            User currentUser, User selectedUser,
                                            IsFollowerObserver observer) {
//        return new IsFollowerTask(authToken, targetUser, pageSize, lastFollower, new PagedHandler<User>(observer, "get followers", GetFollowersTask.FOLLOWERS_KEY));
        return new IsFollowerTask(currUserAuthToken, currentUser, selectedUser,
                                    new IsFollowerHandler(observer));
    }

    /**
     * Returns an instance of {@link FollowTask}. Allows mocking of the
     * FollowTask class for testing purposes. All usages of FollowTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public FollowTask getFollowTask(AuthToken currUserAuthToken, User currentUser,
                                    User selectedUser, FollowObserver observer) {
        return new FollowTask(currUserAuthToken, currentUser.getAlias(), selectedUser.getAlias(),
                new FollowHandler(currUserAuthToken, selectedUser, currentUser, observer));
    }

    /**
     * Returns an instance of {@link UnfollowTask}. Allows mocking of the
     * UnfollowTask class for testing purposes. All usages of UnfollowTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public UnfollowTask getUnfollowTask(AuthToken currUserAuthToken, User currentUser,
                                        User selectedUser, UnfollowObserver observer) {
        return new UnfollowTask(currUserAuthToken, currentUser, selectedUser,
                new UnfollowHandler(currUserAuthToken, selectedUser, currentUser, observer));
    }
}
