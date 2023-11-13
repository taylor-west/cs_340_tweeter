package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";
    private static final String FOLLOWER_TAG = "follower";
    private static final String FOLLOWEE_TAG = "followee";

    /**
     * The alias of the followee (user that is being followed).
     */
    private String followeeAlias;
    /**
     * The alias of the follower (user that will follow the followee).
     */
    private String followerAlias;

    public FollowTask(AuthToken authToken, String followerAlias, String followeeAlias, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    public void performTask() {
        try {
            FollowRequest request = new FollowRequest(followerAlias, followeeAlias);
            FollowResponse response = getServerFacade().follow(request, FollowService.getFollowUrlPath(followerAlias, followeeAlias));

            if (response.isSuccess()) {
                    sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to follow user", ex);
            sendExceptionMessage(ex);
        }
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
