package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";
    private static final String FOLLOWER_TAG = "follower";
    private static final String FOLLOWEE_TAG = "followee";

    /**
     * The user that is being followed.
     */
    private User followee;
    /**
     * The user that will follow the followee
     */
    private User follower;

    public FollowTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    public void performTask() {
//        try {
//            FollowRequest request = new FollowRequest(follower, followee);
//            FollowResponse response = getServerFacade().follow(request, FollowService.URL_PATH);

//            if (response.isSuccess()) {
                    sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (IOException | TweeterRemoteException ex) {
//            Log.e(LOG_TAG, "Failed to follow user", ex);
//            sendExceptionMessage(ex);
//        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
