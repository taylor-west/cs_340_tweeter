package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;

public class GetFollowersCountHandler extends GetCountHandler {
    public GetFollowersCountHandler(UpdateFolloweesAndFollowersObserver observer, String taskDescription) {
        super(observer, taskDescription);
    }

    protected void updateCount(int count) {
        observer.getFollowersCountSuccess(count);
    }
}