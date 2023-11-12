package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFolloweesTask extends PagedTask<User> {

    private static final String LOG_TAG = "GetFolloweesTask";

    public static final String FOLLOWEES_KEY = "followees";
//    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The last person being followed returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected User lastFollowee;
    /**
     * The followee users returned by the server.
     */
    private List<User> followees;
    /**
     * If there are more pages, returned by the server.
     */
    private boolean hasMorePages;

    public GetFolloweesTask(AuthToken authToken, User targetUser,
                            int limit, User lastFollowee, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastFollowee;
    }

    @Override
    public void performTask() {
//        try {
//            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
//            String lastFolloweeAlias = lastFollowee == null ? null : lastItem.getAlias();
//
//            FollowingRequest request = new FollowingRequest(authToken, targetUserAlias, limit, lastFolloweeAlias);
//            FollowingResponse response = getServerFacade().getFollowees(request, FollowService.URL_PATH);
//
//            if (response.isSuccess()) {
//                this.followees = response.getFollowees();
//                this.hasMorePages = response.getHasMorePages();
                Pair<List<User>, Boolean> pageOfItems =  getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
                this.followees = pageOfItems.getFirst();
                this.hasMorePages = pageOfItems.getSecond();
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
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) this.followees);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);

        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }

}
