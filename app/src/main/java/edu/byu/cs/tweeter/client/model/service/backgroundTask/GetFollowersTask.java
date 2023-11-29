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
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String FOLLOWERS_KEY = "followers";

    /**
     * The last follower (person following) returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected User lastFollower;
    /**
     * The followee users returned by the server.
     */
    private List<User> followers;
    /**
     * If there are more pages, returned by the server.
     */
    private boolean hasMorePages;

    public GetFollowersTask(AuthToken authToken, User currUser, User targetUser, int limit, User lastUser,
                            Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.currUser = currUser;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastUser;
    }

    public void performTask() {

        try {
            String currUserAlias = currUser == null ? null : currUser.getAlias();
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            String lastFollowerAlias = lastItem == null ? null : lastItem.getAlias();

            FollowersRequest request = new FollowersRequest(authToken, currUserAlias, limit, lastFollowerAlias, targetUserAlias);
            FollowersResponse response = getServerFacade().getFollowers(request, FollowService.getFollowersUrlPath(targetUser.getAlias()));

            if (response.isSuccess()) {
                this.followers = response.getFollowers();
                this.hasMorePages = response.getHasMorePages();
                    Pair<List<User>, Boolean> pageOfItems =  getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
                    this.followers = pageOfItems.getFirst();
                    this.hasMorePages = pageOfItems.getSecond();
                    sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
        }
    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(FOLLOWERS_KEY, (Serializable) this.followers);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);

        return msgBundle;
    }


    protected String getLogTag() {
        return LOG_TAG;
    }
}
