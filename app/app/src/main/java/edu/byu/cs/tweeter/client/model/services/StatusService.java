package edu.byu.cs.tweeter.client.model.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public interface GetFeedObserver {
        void getFeedSucceeded(List<Status> statuses, boolean hasMorePages);
        void getFeedFailed(String message); // takes the error message produced by a failure to getUser
    }

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, GetFeedObserver observer){
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, pageSize,
                lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends Handler {
        private GetFeedObserver observer;

        public GetFeedHandler(GetFeedObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.getFeedSucceeded(statuses, hasMorePages);

            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.getFeedFailed("Failed to get feed: " + message);

            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.getFeedFailed("Failed to get feed because of exception: " + ex.getMessage());
            }
        }
    }

    public interface GetStoryObserver {
        void getStorySucceeded(List<Status> statuses, boolean hasMorePages);
        void getStoryFailed(String message); // takes the error message produced by a failure to getUser
    }

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, GetStoryObserver observer){
        String test = "test";
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, pageSize,
                lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends Handler {
        GetStoryObserver observer;
        public GetStoryHandler(GetStoryObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                observer.getStorySucceeded(statuses, hasMorePages);

            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.getStoryFailed("Failed to get story: " + message);

            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.getStoryFailed("Failed to get story because of exception: " + ex.getMessage());
            }
        }
    }

    public interface PostStatusObserver{

    }

    public void postStatus(){

    }

    private class PostStatusHandler{

    }
}


