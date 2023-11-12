package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface FollowObserver extends ServiceObserver {
    void startingFollow(User selectedUser);

    void endingFollow();

    void followSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser);
}