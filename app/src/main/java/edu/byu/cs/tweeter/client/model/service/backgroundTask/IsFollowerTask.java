package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

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

    public IsFollowerTask(AuthToken authToken, User currUser, User follower, User followee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currUser = currUser;
        this.follower = follower;
        this.followee = followee;
    }

    public void performTask() {
        this.isFollower = new Random().nextInt() > 0;
        sendSuccessMessage();

        try {
            String currUserAlias = currUser == null ? null : currUser.getAlias();
            String followerAlias = follower == null ? null : follower.getAlias();
            String followeeAlias = followee == null ? null : followee.getAlias();

            IsFollowerRequest request = new IsFollowerRequest(authToken, currUserAlias, followerAlias, followeeAlias);
            IsFollowerResponse response = getServerFacade().isFollower(request, FollowService.getIsFollowerUrlPath(follower.getAlias(), followee.getAlias()));

            if (response.isSuccess()) {
                this.isFollower = response.isFollower();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to determine if is follower", ex);
            sendExceptionMessage(ex);
        }
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
