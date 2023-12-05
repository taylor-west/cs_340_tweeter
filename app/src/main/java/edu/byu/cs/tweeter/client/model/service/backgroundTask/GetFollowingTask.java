package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {

    private static final String LOG_TAG = "GetFollowingTask";

    public static final String FOLLOWEES_KEY = "followees";

    /**
     * The followee users returned by the server.
     */
    private List<User> followees;
    private Boolean hasMorePages;


    public GetFollowingTask(AuthToken authToken, User targetUser,
                            int limit, User lastFollowee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastFollowee;
    }

    @Override
    public void performTask() {
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            String lastFolloweeAlias = lastItem == null ? null : lastItem.getAlias();

            FollowingRequest request = new FollowingRequest(authToken, limit, lastFolloweeAlias, targetUserAlias);
            FollowingResponse response = getServerFacade().getFollowing(request, FollowService.getFollowingUrlPath(targetUserAlias));

            if (response.isSuccess()) {
                this.followees = response.getFollowing();
//                this.lastItem = response.getFollowing().get(response.getFollowing().size() - 1);
                this.hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) this.followees);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);

        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }

}
