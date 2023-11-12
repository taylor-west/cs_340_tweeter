package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String FOLLOWERS_KEY = "followers";

    private List<User> followers;
    private boolean hasMorePages;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastUser,
                            Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastUser;
    }

    public void performTask() {

        //        try {
//            GetFollowersRequest request = new GetFollowersRequest(...);
//            GetFollowersResponse response = getServerFacade().getFollowers(request, FollowService.URL_PATH);

//            if (response.isSuccess()) {
//                this.followers = response.getFollowers();
//                this.hasMorePages = response.getHasMorePages();
                    Pair<List<User>, Boolean> pageOfItems =  getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
                    this.followers = pageOfItems.getFirst();
                    this.hasMorePages = pageOfItems.getSecond();
                    sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (IOException | TweeterRemoteException ex) {
//            Log.e(LOG_TAG, "Failed to get followers", ex);
//            sendExceptionMessage(ex);
//        }
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
