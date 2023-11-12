package edu.byu.cs.tweeter.client.presenter.observers;

import java.util.List;

public interface PagedObserver<T> extends ServiceObserver {
    void getSucceeded(List<T> items, boolean hasMorePages);
}