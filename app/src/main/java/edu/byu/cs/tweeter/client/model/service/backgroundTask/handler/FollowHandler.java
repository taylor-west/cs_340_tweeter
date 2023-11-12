package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.presenter.observers.FollowObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends ServiceHandler<FollowObserver> {
    private User selectedUser;
    private User currUser;
    private AuthToken currUserAuthToken;

    public FollowHandler(AuthToken currUserAuthToken, User selectedUser, User currUser,
                         FollowObserver observer) {
        super(observer, "follow");
        this.currUserAuthToken = currUserAuthToken;
        this.selectedUser = selectedUser;
        this.currUser = currUser;
        this.observer = observer;
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.followSucceeded(this.currUserAuthToken, this.currUser, this.selectedUser);
    }
}

