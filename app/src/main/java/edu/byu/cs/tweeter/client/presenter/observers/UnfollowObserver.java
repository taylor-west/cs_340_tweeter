package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface UnfollowObserver extends ServiceObserver {
    void startingUnfollow(User selectedUser);

    void endingUnfollow();

    void unfollowSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser);
}