package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";
    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String targetUserAlias;

    private User targetUser;

    public GetUserTask(AuthToken authToken, User currUser, String targetUserAlias, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currUser = currUser;
        this.targetUserAlias = targetUserAlias;
    }

    public void performTask() {
        try {
            String currUserAlias = currUser == null ? null : currUser.getAlias();

            GetUserRequest request = new GetUserRequest(authToken, currUserAlias, targetUserAlias);
            GetUserResponse response = getServerFacade().getUser(request, UserService.getGetUserUrlPath(targetUserAlias));

            if (response.isSuccess()) {
                this.targetUser = response.getUser();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get user", ex);
            sendExceptionMessage(ex);
        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, this.targetUser);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
