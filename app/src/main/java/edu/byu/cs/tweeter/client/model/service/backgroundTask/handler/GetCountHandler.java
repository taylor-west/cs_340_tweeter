package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;

abstract public class GetCountHandler extends ServiceHandler<UpdateFolloweesAndFollowersObserver> {

    protected abstract void updateCount(int count);

    public GetCountHandler(UpdateFolloweesAndFollowersObserver observer, String taskDescription) {
        super(observer, taskDescription);
    }

    @Override
    protected void handleSuccess(Message msg) {
        int count = msg.getData().getInt(GetCountTask.COUNT_KEY);
        updateCount(count);
    }
}
