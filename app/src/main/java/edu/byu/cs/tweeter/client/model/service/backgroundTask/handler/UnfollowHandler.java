package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.presenter.observers.UnfollowObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends ServiceHandler<UnfollowObserver> {
    private User selectedUser;
    private User currUser;
    private AuthToken currUserAuthToken;

    public UnfollowHandler(AuthToken currUserAuthToken, User selectedUser, User currUser,
                           UnfollowObserver observer) {
        super(observer, "unfollow");
        this.currUserAuthToken = currUserAuthToken;
        this.selectedUser = selectedUser;
        this.currUser = currUser;
        this.observer = observer;
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.unfollowSucceeded(this.currUserAuthToken, this.currUser, this.selectedUser);
    }
}
