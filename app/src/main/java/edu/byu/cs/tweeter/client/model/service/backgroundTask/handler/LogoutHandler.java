package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;


import android.os.Message;

import edu.byu.cs.tweeter.client.presenter.observers.LogoutObserver;

public class LogoutHandler extends ServiceHandler<LogoutObserver> {

    public LogoutHandler(LogoutObserver observer) {
        super(observer, "logout");
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.logoutSucceeded();
    }
}

