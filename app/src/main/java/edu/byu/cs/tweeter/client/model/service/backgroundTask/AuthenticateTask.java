package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

public abstract class AuthenticateTask extends BackgroundTask {
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected String alias;
    protected String password;

    protected AuthenticateTask(Handler messageHandler) {
        super(messageHandler);
    }
}
