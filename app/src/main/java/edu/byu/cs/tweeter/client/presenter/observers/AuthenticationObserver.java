package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticationObserver extends ServiceObserver {
    void authenticationSucceeded(AuthToken authToken, User user);
}