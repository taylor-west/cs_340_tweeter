package edu.byu.cs.tweeter.client.presenter.observers;

public interface ServiceObserver {
    void handleFailure(String message);

    void handleExceptions(String message);
}


// TODO: structure follows the handlers pretty closely
// observers are created by presenters and sent up to the services
// service takes the observer, creates a handler and feeds the handler to the task (running the task)
