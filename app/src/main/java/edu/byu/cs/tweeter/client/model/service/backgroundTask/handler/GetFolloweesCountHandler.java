package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;

public class GetFolloweesCountHandler extends GetCountHandler {
    public GetFolloweesCountHandler(UpdateFolloweesAndFollowersObserver observer, String taskDescription) {
        super(observer, taskDescription);
    }

    protected void updateCount(int count) {
        observer.getFolloweesCountSuccess(count);
    }
}


