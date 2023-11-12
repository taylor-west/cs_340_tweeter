package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {
    private static final String LOG_TAG = "RegisterTask";

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    private User registeredUser;

    private AuthToken registeredAuthToken;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler);

        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = username;
        this.password = password;
        this.image = image;
    }

    public void performTask() {
        Pair<User, AuthToken> registerResult = doRegister();

        this.registeredUser = registerResult.getFirst();
        this.registeredAuthToken = registerResult.getSecond();

        sendSuccessMessage();

        //        try {
//            _______Request request = new _______Request(...);
//            _______Response response = getServerFacade().__________(request, _______Service.URL_PATH);

//            if (response.isSuccess()) {
//                this.followees = response.getFollowees();
//                this.hasMorePages = response.getHasMorePages();
        sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (IOException | TweeterRemoteException ex) {
//            Log.e(LOG_TAG, "Failed to get followees", ex);
//            sendExceptionMessage(ex);
//        }
    }

    private Pair<User, AuthToken> doRegister() {
        User registeredUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(registeredUser, authToken);
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, this.registeredUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, this.registeredAuthToken);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
