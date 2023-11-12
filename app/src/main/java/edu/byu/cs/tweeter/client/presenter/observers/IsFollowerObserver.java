package edu.byu.cs.tweeter.client.presenter.observers;

public interface IsFollowerObserver extends ServiceObserver {
    void isFollowerSucceeded(boolean isFollower);
}
