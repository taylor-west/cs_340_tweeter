package edu.byu.cs.tweeter.client.model.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public FollowService() {
    }

    public interface GetFollowingObserver{
        void getFollowingSucceeded(List<User> followees, boolean hasMorePages);
        void getFollowingFailed(String message); // takes the error message produced by a failure to getFollowing
    }

    public void getFollowing(AuthToken authToken, User user, int pageSize,
                             User lastFollowee, GetFollowingObserver observer){

        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {
        private GetFollowingObserver observer;
        public GetFollowingHandler(GetFollowingObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                User lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
                observer.getFollowingSucceeded(followees, hasMorePages);

            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.getFollowingFailed("Failed to get following: " + message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.getFollowingFailed("Failed to get following because of exception: " + ex.getMessage());
            }
        }
    }


    public interface GetFollowersObserver{
        void getFollowersSucceeded(List<User> followers, boolean hasMorePages);
        void getFollowersFailed(String message); // takes the error message produced by a failure to getFollowing
    }

    public void getFollowers(AuthToken authToken, User user, int pageSize,
                             User lastFollower, GetFollowersObserver observer){

        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private GetFollowersObserver observer;
        public GetFollowersHandler(GetFollowersObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                User lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
                observer.getFollowersSucceeded(followers, hasMorePages);

            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.getFollowersFailed("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.getFollowersFailed("Failed to get followers because of exception: " + ex.getMessage());
            }
        }
    }


    public interface IsFollowerObserver{
        void isFollowerSucceeded(boolean isFollower);
        void isFollowerFailed(String message);
    }

    public void isFollower(AuthToken currUserAuthToken, User currentUser, User selectedUser, IsFollowerObserver observer){
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currentUser, selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    // IsFollowerHandler
    private class IsFollowerHandler extends Handler {
        private IsFollowerObserver observer;

        public IsFollowerHandler(IsFollowerObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.isFollowerSucceeded(isFollower);

            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.isFollowerFailed("Failed to determine following relationship: " + message);

            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.isFollowerFailed("Failed to determine following relationship because of exception: " + ex.getMessage());
            }
        }
    }

    public interface FollowObserver{
        void startingFollow(User selectedUser);
        void endingFollow();
        void followSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser);
        void followFailed(String message);
        void enableFollowButton();
    }

    public void follow(AuthToken currUserAuthToken, User selectedUser, User currUser, FollowObserver observer){

        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(currUserAuthToken, selectedUser, currUser, observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);

        observer.startingFollow(selectedUser);
    }

    // FollowHandler

    private class FollowHandler extends Handler {
        private User selectedUser;
        private User currUser;
        private AuthToken currUserAuthToken;
        private FollowObserver observer;


        public FollowHandler(AuthToken currUserAuthToken, User selectedUser, User currUser,
                               FollowObserver observer) {
            super(Looper.getMainLooper());
            this.currUserAuthToken = currUserAuthToken;
            this.selectedUser = selectedUser;
            this.currUser = currUser;
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            observer.endingFollow();

            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.followSucceeded(this.currUserAuthToken, this.currUser, this.selectedUser);

            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.followFailed("Failed to follow: " + message);

            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.followFailed("Failed to follow because of exception: " + ex.getMessage());
            }

            observer.enableFollowButton();
        }
    }



    public interface UnfollowObserver{
        void startingUnfollow(User selectedUser);
        void endingUnfollow();
        void unfollowSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser);
        void unfollowFailed(String message);
        void enableFollowButton();
    }

    public void unfollow(AuthToken currUserAuthToken, User selectedUser, User currUser, UnfollowObserver observer){
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(currUserAuthToken, selectedUser, currUser, observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
        observer.startingUnfollow(selectedUser);
    }

    // UnfollowHandler

    private class UnfollowHandler extends Handler {
        private User selectedUser;
        private User currUser;
        private AuthToken currUserAuthToken;
        private UnfollowObserver observer;


        public UnfollowHandler(AuthToken currUserAuthToken, User selectedUser, User currUser,
                               UnfollowObserver observer) {
            super(Looper.getMainLooper());
            this.currUserAuthToken = currUserAuthToken;
            this.selectedUser = selectedUser;
            this.currUser = currUser;
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            observer.endingUnfollow();

            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.unfollowSucceeded(this.currUserAuthToken, this.currUser, this.selectedUser);

            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.unfollowFailed("Failed to unfollow: " + message);

            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.unfollowFailed("Failed to unfollow because of exception: " + ex.getMessage());
            }

            observer.enableFollowButton();
        }
    }

}
