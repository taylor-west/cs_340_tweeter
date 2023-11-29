package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";

    private User targetUser;
    private Status status;

    public PostStatusTask(AuthToken authToken, User currUser, User targetUser, Status status, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currUser = currUser;
        this.targetUser = targetUser;
        this.status = status;
    }

    public void performTask() {
        try {
            String currUserAlias = currUser == null ? null : currUser.getAlias();
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            PostStatusRequest request = new PostStatusRequest(authToken, currUserAlias, targetUserAlias, status);
            PostStatusResponse response = getServerFacade().postStatus(request, StatusService.getPostStatusUrlPath(targetUser.getAlias()));

            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to post status", ex);
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
