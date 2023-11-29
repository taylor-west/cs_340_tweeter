package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatedTask extends BackgroundTask {
    /**
     * Auth token for logged-in user.
     */
    protected AuthToken authToken;

    protected User currUser;

    protected AuthenticatedTask(Handler messageHandler) {
        super(messageHandler);
    }
}
