package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask {
    public static final String COUNT_KEY = "count";
    protected User targetUser;

    protected GetCountTask(Handler messageHandler) {
        super(messageHandler);
    }
}
