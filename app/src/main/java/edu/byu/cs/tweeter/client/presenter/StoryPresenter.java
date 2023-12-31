package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> implements PagedObserver<Status> {
    public interface StoryView extends PagedView<Status> {
    }

    public StoryPresenter(View view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }

    protected void getItems(AuthToken authToken, User currUser, User targerUser, int pageSize, Status lastItem) {
        this.statusService = getStatusService();
        statusService.getStory(authToken, currUser, targerUser, PAGE_SIZE, (Status) lastItem, this);
    }
}
