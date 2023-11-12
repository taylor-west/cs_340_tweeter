package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.presenter.observers.AuthenticationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationHandler extends ServiceHandler<AuthenticationObserver> {
    public AuthenticationHandler(AuthenticationObserver observer, String taskDescription) {
        super(observer, taskDescription);
    }

    @Override
    protected void handleSuccess(Message msg) {
        User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

        // Cache user session information
        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.authenticationSucceeded(authToken, loggedInUser);
    }
}