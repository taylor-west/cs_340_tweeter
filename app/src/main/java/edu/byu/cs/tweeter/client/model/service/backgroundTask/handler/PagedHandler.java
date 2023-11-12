package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;

public class PagedHandler<T> extends ServiceHandler<PagedObserver<T>> {

    protected String itemKey;

    public PagedHandler(PagedObserver<T> observer, String taskDescription, String key) {
        super(observer, taskDescription);
        this.itemKey = key;
    }

    @Override
    protected void handleSuccess(Message msg) {
        List<T> items = (List<T>) msg.getData().getSerializable(this.itemKey);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
        this.observer.getSucceeded(items, hasMorePages);
    }
}
