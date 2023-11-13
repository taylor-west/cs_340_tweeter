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

    public FeedPresenter(View view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }

    protected void getItems(AuthToken authToken, User user, int pageSize, Status lastStatus) {
//        this.statusService = getStatusService();
        StatusService statusService = new StatusService();
        statusService.getFeed(authToken, user, pageSize, (Status) lastStatus, this);
    }
}
