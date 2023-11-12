package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetStoryTask";

    public static final String STATUSES_KEY = "statuses";

    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
//    private Status lastItem;

    private List<Status> statuses;

    private boolean hasMorePages;


    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastStatus;
    }

    public void performTask() {


        //        try {
//            _______Request request = new _______Request(...);
//            _______Response response = getServerFacade().__________(request, _______Service.URL_PATH);

//            if (response.isSuccess()) {
//                this.followees = response.getFollowees();
//                this.hasMorePages = response.getHasMorePages();
                    Pair<List<Status>, Boolean> pageOfItems = getFakeData().getPageOfStatus(lastItem, limit);
                    this.statuses = pageOfItems.getFirst();
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

//    private Pair<List<Status>, Boolean> getStory() {
//        return getFakeData().getPageOfStatus(lastItem, limit);
////        return pageOfStatus;
//    }

    protected Bundle constructSuccessBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(STATUSES_KEY, (Serializable) this.statuses);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);
        return msgBundle;
    }

    protected String getLogTag() {
        return LOG_TAG;
    }
}