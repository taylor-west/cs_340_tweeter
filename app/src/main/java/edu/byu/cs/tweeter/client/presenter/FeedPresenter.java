package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.observers.GetUserObserver;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> implements GetUserObserver, PagedObserver<Status> {
    public interface FeedView extends PagedView<Status> {
    }

    public FeedPresenter(View view, User user) {
        this.view = view;
        this.targetUser = user;
    }

    protected void getItems(AuthToken authToken, User targerUser, int pageSize, Status lastItem) {
//        this.statusService = getStatusService();
        StatusService statusService = new StatusService();
        statusService.getFeed(authToken, targetUser, PAGE_SIZE, (Status) lastItem, this);
    }
}
