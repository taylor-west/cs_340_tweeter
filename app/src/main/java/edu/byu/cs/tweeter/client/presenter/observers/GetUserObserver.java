package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.model.domain.User;

public interface GetUserObserver extends ServiceObserver {
    void getUserSucceeded(User user);
}
