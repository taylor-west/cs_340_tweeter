package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> implements PagedObserver<User> {
    public interface FollowersView extends PagedView<User> {
    }

    public FollowersPresenter(View view, User user) {
        this.view = view;
        this.targetUser = user;
    }

    protected void getItems(AuthToken authToken, User targerUser, int pageSize, User lastItem) {
        FollowService followService = new FollowService();
        followService.getFollowers(authToken, targetUser, PAGE_SIZE, (User) lastItem, this);
    }
}
