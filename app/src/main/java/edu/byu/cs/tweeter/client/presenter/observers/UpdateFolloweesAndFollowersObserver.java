package edu.byu.cs.tweeter.client.presenter.observers;

public interface UpdateFolloweesAndFollowersObserver extends ServiceObserver {
    void getFollowersCountSuccess(int followerCount);

    void getFolloweesCountSuccess(int followeesCount);
}
