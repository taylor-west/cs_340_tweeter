package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFolloweesCountTask extends GetCountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    private int count;

    public GetFolloweesCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    public void performTask() {

        //        try {
//            _______Request request = new _______Request(...);
//            _______Response response = getServerFacade().__________(request, _______Service.URL_PATH);

//            if (response.isSuccess()) {
//                this.followees = response.getFollowees();
//                this.hasMorePages = response.getHasMorePages();
                    this.count = 20;
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
        msgBundle.putInt(COUNT_KEY, this.count);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}
