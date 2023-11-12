package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.presenter.observers.GetUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends ServiceHandler<GetUserObserver> {
    public GetUserHandler(GetUserObserver observer) {
        super(observer, "get user's profile");
    }

    @Override
    protected void handleSuccess(Message msg) {
        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        observer.getUserSucceeded(user);
    }
}

