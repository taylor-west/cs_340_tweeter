package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    private int count;

    public GetFollowingCountTask(AuthToken authToken, User currUser, User targetUser, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currUser = currUser;
        this.targetUser = targetUser;
    }

    public void performTask() {

        try {
            String currUserAlias = currUser == null ? null : currUser.getAlias();
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            GetFollowingCountRequest request = new GetFollowingCountRequest(authToken, currUserAlias, targetUserAlias);
            GetFollowingCountResponse response = getServerFacade().getFollowingCount(request, FollowService.getFollowingCountUrlPath(targetUser.getAlias()));

            if (response.isSuccess()) {
                this.count = response.getFolloweesCount();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get following count", ex);
            sendExceptionMessage(ex);
        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putInt(COUNT_KEY, this.count);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
