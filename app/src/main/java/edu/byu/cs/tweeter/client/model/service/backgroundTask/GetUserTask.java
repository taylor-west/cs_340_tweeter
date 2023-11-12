package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";
    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.alias = alias;
    }

    public void performTask() {

        //        try {
//            _______Request request = new _______Request(...);
//            _______Response response = getServerFacade().__________(request, _______Service.URL_PATH);

//            if (response.isSuccess()) {
//                this.followees = response.getFollowees();
//                this.hasMorePages = response.getHasMorePages();
                    this.user = getFakeData().findUserByAlias(alias);
                    sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (IOException | TweeterRemoteException ex) {
//            Log.e(LOG_TAG, "Failed to get followees", ex);
//            sendExceptionMessage(ex);
//        }
    }

    private User getUser() {
        this.user = getFakeData().findUserByAlias(alias);
        return user;
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, this.user);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
