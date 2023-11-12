package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> implements PagedObserver<Status> {
    public interface StoryView extends PagedView<Status> {
    }

    public StoryPresenter(View view, User user) {
        this.view = view;
        this.targetUser = user;
    }

    protected void getItems(AuthToken authToken, User targerUser, int pageSize, Status lastItem) {
        this.statusService = getStatusService();
        statusService.getStory(authToken, targerUser, PAGE_SIZE, (Status) lastItem, this);
    }
}
