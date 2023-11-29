package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

//    public static final String USER_KEY = "user";
//    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String alias;

    /**
     * The user's password.
     */
    private String password;

    /**
     * The logged-in user returned by the server.
     */
    protected User loggedInUser;

    /**
     * The auth token returned by the server.
     */
    protected AuthToken loggedInAuthToken;


    public LoginTask(UserService userService, String alias, String password, Handler messageHandler) {
        super(messageHandler);

        this.alias = alias;
        this.password = password;
    }

    @Override
    protected void performTask() {
        try {
            LoginRequest request = new LoginRequest(alias, password);
            LoginResponse response = getServerFacade().login(request, UserService.getLoginUrlPath());

            if (response.isSuccess()) {
                this.loggedInUser = response.getUser();
                this.loggedInAuthToken = response.getAuthToken();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, this.loggedInUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, this.loggedInAuthToken);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
