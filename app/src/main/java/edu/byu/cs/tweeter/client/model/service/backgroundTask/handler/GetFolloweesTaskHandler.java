//package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//
//import java.util.List;
//
//import edu.byu.cs.tweeter.client.model.service.FollowService;
//import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFolloweesTask;
//import edu.byu.cs.tweeter.model.domain.User;
//
///**
// * Handles messages from the background task indicating that the task is done, by invoking
// * methods on the observer.
// */
//public class GetFolloweesTaskHandler extends Handler {
//
//    private final FollowService.GetFollowingObserver observer;
//
//    public GetFolloweesTaskHandler(FollowService.GetFollowingObserver observer) {
//        super(Looper.getMainLooper());
//        this.observer = observer;
//    }
//
//    @Override
//    public void handleMessage(Message message) {
//        Bundle bundle = message.getData();
//        boolean success = bundle.getBoolean(GetFolloweesTask.SUCCESS_KEY);
//        if (success) {
//            List<User> followees = (List<User>) bundle.getSerializable(GetFolloweesTask.FOLLOWEES_KEY);
//            boolean hasMorePages = bundle.getBoolean(GetFolloweesTask.MORE_PAGES_KEY);
//            observer.handleSuccess(followees, hasMorePages);
//        } else if (bundle.containsKey(GetFolloweesTask.MESSAGE_KEY)) {
//            String errorMessage = bundle.getString(GetFolloweesTask.MESSAGE_KEY);
//            observer.handleFailure(errorMessage);
//        } else if (bundle.containsKey(GetFolloweesTask.EXCEPTION_KEY)) {
//            Exception ex = (Exception) bundle.getSerializable(GetFolloweesTask.EXCEPTION_KEY);
//            observer.handleException(ex);
//        }
//    }
//}
