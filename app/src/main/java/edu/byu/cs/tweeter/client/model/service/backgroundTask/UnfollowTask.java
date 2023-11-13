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
     * The user that is being unfollowed.
     */
    private User targetUser;

    /**
     * The user that is unfollowing the targetUser
     */
    private User currentUser;

    public UnfollowTask(AuthToken authToken, User currentUser, User targetUser, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currentUser = currentUser;
        this.targetUser = targetUser;
    }

    public void performTask() {
        try {
            UnfollowRequest request = new UnfollowRequest(currentUser.getAlias(), targetUser.getAlias());
            UnfollowResponse response = getServerFacade().unfollow(request, FollowService.getUnfollowUrlPath(currentUser.getAlias(), targetUser.getAlias()));

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
