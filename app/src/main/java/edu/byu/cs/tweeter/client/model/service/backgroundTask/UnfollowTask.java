package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";

    /**
     * The user that is unfollowing the followee
     */
    private User follower;

    /**
     * The user that is being unfollowed hy the follower.
     */
    private User followee;

    public UnfollowTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    public void performTask() {
        try {
            UnfollowRequest request = new UnfollowRequest(authToken, follower.getAlias(), followee.getAlias());
            UnfollowResponse response = getServerFacade().unfollow(request, FollowService.getUnfollowUrlPath(follower.getAlias(), followee.getAlias()));

            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to unfollow", ex);
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
