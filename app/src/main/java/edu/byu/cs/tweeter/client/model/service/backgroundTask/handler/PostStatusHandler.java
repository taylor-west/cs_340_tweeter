package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.presenter.observers.PostStatusObserver;

public class PostStatusHandler extends ServiceHandler<PostStatusObserver> {
    public PostStatusHandler(PostStatusObserver observer) {
        super(observer, "post status");
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.postStatusSucceeded();
    }
}
