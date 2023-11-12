package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.presenter.observers.IsFollowerObserver;

public class IsFollowerHandler extends ServiceHandler<IsFollowerObserver> {
    public IsFollowerHandler(IsFollowerObserver observer) {
        super(observer, "determine following relationship");
    }

    @Override
    protected void handleSuccess(Message msg) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.isFollowerSucceeded(isFollower);
    }
}

