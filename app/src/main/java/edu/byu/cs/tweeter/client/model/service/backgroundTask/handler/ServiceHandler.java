package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.presenter.observers.ServiceObserver;

abstract class ServiceHandler<T extends ServiceObserver> extends Handler {
    protected T observer;

    protected String taskDescription;

    protected abstract void handleSuccess(Message msg);

    public ServiceHandler(T observer, String taskDescription) {
        super(Looper.getMainLooper());
        this.observer = observer;
        this.taskDescription = taskDescription;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccess(msg);
        } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            this.observer.handleFailure("Failed to " + taskDescription + ": " + message);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            this.observer.handleExceptions("Failed to " + taskDescription + " because of exception: " + ex.getMessage());
        }
    }
}



