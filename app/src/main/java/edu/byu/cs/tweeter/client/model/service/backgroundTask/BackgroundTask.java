package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {

    private static final String LOG_TAG = "Task";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    protected final Handler messageHandler;
    protected ServerFacade serverFacade;

    protected abstract void performTask();

    protected abstract Bundle constructSuccessBundle();

    protected abstract String getLogTag();

    protected BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            performTask();
        } catch (Exception ex) {
            Log.e(getLogTag(), ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return new ServerFacade();
    }

    // This method is public instead of protected to make it accessible to test cases
    public void sendSuccessMessage() {
        Message msg = Message.obtain();
        msg.setData(constructSuccessBundle());

        messageHandler.sendMessage(msg);
    }

    // This method is public instead of protected to make it accessible to test cases
    public void sendFailedMessage(String message) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, message);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    // This method is public instead of protected to make it accessible to test cases
    public void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}