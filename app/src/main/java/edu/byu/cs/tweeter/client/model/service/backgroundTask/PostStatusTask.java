package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";

    private User user;
    private Status status;

    public PostStatusTask(AuthToken authToken, User user, Status status, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.user = user;
        this.status = status;
    }

    public void performTask() {
//        try {
//            PostStatusRequest request = new PostStatusRequest(user, status);
//            PostStatusResponse response = getServerFacade().postStatus(request, StatusService.URL_PATH);

//            if (response.isSuccess()) {
        sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (IOException | TweeterRemoteException ex) {
//            Log.e(LOG_TAG, "Failed to post status", ex);
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
