package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.presenter.observers.AuthenticationObserver;
import edu.byu.cs.tweeter.client.presenter.observers.GetUserObserver;
import edu.byu.cs.tweeter.client.presenter.observers.LogoutObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService {

//    public static final String URL_PATH = "/login";
    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String USER_URL_PATH = "/user";
    public static final String LOGOUT_URL_PATH = "/logout";
//    public static final String UPDATE_FOLLOWERS_FOLLOWING_URL_PATH = "/followersfollowing";

//    /**
//     * An observer interface to be implemented by observers who want to be notified when
//     * asynchronous operations complete.
//     */
//    public interface LoginObserver {
//        void handleSuccess(User user, AuthToken authToken);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    /**
     * Creates an instance.
     *
     */
     public UserService() {
     }


    /**
     * Makes an asynchronous login request.
     *
     * @param alias the user's name.
     * @param password the user's password.
     */
    public void login(String alias, String password, AuthenticationObserver observer) {
        LoginTask loginTask = getLoginTask(alias, password, observer);
        BackgroundTaskUtils.runTask(loginTask);
    }

    public static String getLoginUrlPath(){
        return LOGIN_URL_PATH;
    }

    /**
     * Returns an instance of {@link LoginTask}. Allows mocking of the LoginTask class for
     * testing purposes. All usages of LoginTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public LoginTask getLoginTask(String alias, String password, AuthenticationObserver observer) {
//        return new LoginTask(this, alias, password, new LoginTaskHandler(observer));
        return new LoginTask(this, alias, password, new AuthenticationHandler(observer, "login"));
    }

    /**
     * Makes an asynchronous register request.
     * @param firstName the first name of the user to be registered
     * @param lastName the last name of the user to be registered
     * @param alias the alias of the user to be registered
     * @param password the password of the user to be registered
     * @param imageBytesBase64 the base64 string encoding of profile photo of the of the user to be registered
     */
    public void register(String firstName, String lastName, String alias,
                         String password, String imageBytesBase64, AuthenticationObserver observer) {
        RegisterTask registerTask = getRegisterTask(firstName, lastName, alias, password, imageBytesBase64, observer);
        BackgroundTaskUtils.runTask(registerTask);
    }

    public static String getRegisterUrlPath(){
        return REGISTER_URL_PATH;
    }

    /**
     * Returns an instance of {@link RegisterTask}. Allows mocking of the RegisterTask class for
     * testing purposes. All usages of RegisterTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public RegisterTask getRegisterTask(String firstName, String lastName, String alias,
                              String password, String imageBytesBase64, AuthenticationObserver observer){
        return new RegisterTask(firstName, lastName, alias, password, imageBytesBase64,
                new AuthenticationHandler(observer, "register"));
    }

    /**
     * Makes an asynchronous request to get a user's information
     * @param authToken
     * @param alias the alias of the target user
     */
    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = getGetUserTask(authToken, alias, observer);
        BackgroundTaskUtils.runTask(getUserTask);
    }

    public static String getGetUserUrlPath(String userAlias) {
        return USER_URL_PATH + "/" + userAlias;
    }

    /**
     * Returns an instance of {@link GetUserTask}. Allows mocking of the GetUserTask class for
     * testing purposes. All usages of GetUserTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public GetUserTask getGetUserTask(AuthToken authToken, String alias, GetUserObserver observer){
        return new GetUserTask(authToken, alias, new GetUserHandler(observer));
    }

    /**
     * Makes an asynchoronous logout request
     * @param authToken
     */
    public void logout(AuthToken authToken, LogoutObserver observer) {
        LogoutTask logoutTask = getLogoutTask(authToken, observer);
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public static String getLogoutUrlPath() {
        return LOGOUT_URL_PATH;
    }

    /**
     * Returns an instance of {@link LogoutTask}. Allows mocking of the LogoutTask class for
     * testing purposes. All usages of LogoutTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public LogoutTask getLogoutTask(AuthToken authToken, LogoutObserver observer){
        return new LogoutTask(authToken, new LogoutHandler(observer));
    }

    public void updateFollowingAndFollowers(AuthToken authToken,
                                            User user, UpdateFolloweesAndFollowersObserver observer) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = getGetFollowersCountTask(authToken, user, observer);
        BackgroundTaskUtils.runTask(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followeesCountTask = getGetFollowingCountTask(authToken, user, observer);
        BackgroundTaskUtils.runTask(followeesCountTask);

    }

    /**
     * Returns an instance of {@link GetFollowersCountTask}. Allows mocking of the GetFollowersCountTask class for
     * testing purposes. All usages of GetFollowersCountTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    GetFollowersCountTask getGetFollowersCountTask(AuthToken authToken, User user, UpdateFolloweesAndFollowersObserver observer){
        return new GetFollowersCountTask(authToken, user, new GetFollowersCountHandler(observer, "get followers count"));
    }

    /**
     * Returns an instance of {@link GetFollowingCountTask}. Allows mocking of the GetFollowgetFollowingCountTaskingCountTask class for
     * testing purposes. All usages of getFollowingCountTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    GetFollowingCountTask getGetFollowingCountTask(AuthToken authToken, User user, UpdateFolloweesAndFollowersObserver observer){
        return new GetFollowingCountTask(authToken, user, new GetFollowingCountHandler(observer, "get followees count"));
    }
}
