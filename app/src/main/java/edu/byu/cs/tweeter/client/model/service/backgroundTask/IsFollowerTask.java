package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {
    private static final String LOG_TAG = "IsFollowerTask";
    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;

    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    public void performTask() {
        this.isFollower = new Random().nextInt() > 0;
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

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putBoolean(IS_FOLLOWER_KEY, this.isFollower);

        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
