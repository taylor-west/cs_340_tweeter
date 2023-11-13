package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;

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


        try {
            GetStoryRequest request = new GetStoryRequest(authToken, targetUser.getAlias(), limit, lastItem);
            GetStoryResponse response = getServerFacade().getStory(request, StatusService.getStoryUrl(targetUser.getAlias()));

            if (response.isSuccess()) {
                this.statuses = response.getStatuses();
                this.hasMorePages = response.getHasMorePages();
//                    Pair<List<Status>, Boolean> pageOfItems = getFakeData().getPageOfStatus(lastItem, limit);
//                    this.statuses = pageOfItems.getFirst();
//                    this.hasMorePages = pageOfItems.getSecond();

                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get story: ", ex);
            sendExceptionMessage(ex);
        }
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