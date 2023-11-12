package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

public class MainPresenter extends AppCompatActivity implements   //StatusDialogFragment.Observer,
                                        UserService.LogoutObserver,
                                        UserService.UpdateFollowingAndFollowersObserver,
                                        FollowService.IsFollowerObserver,
                                        FollowService.FollowObserver,
                                        FollowService.UnfollowObserver{
    private User currUser;
    private View view;
    public MainPresenter(User user, View view) {
        this.currUser = user;
        this.view = view;
    }

    public void validateUser(){
        if(this.currUser == null){
            view.showErrorMessage("User not passed to activity");
        }
    }

    public void logout(AuthToken currUserAuthToken){
        view.showInfoMessage("Logging Out...");

        var userService = new UserService();
        userService.logout(currUserAuthToken, this);
    }

    @Override
    public void logoutSucceeded() {
        view.hideInfoMessage();
        view.openLoginView();
    }

    @Override
    public void logoutFailed(String message) {
        view.hideInfoMessage();
        view.showErrorMessage(message);
    }


    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();

        void openLoginView();

        void setFolloweesCount(int followeesCount);
        void setFollowersCount(int followersCount);

        void hideFollowButton();
        void showFollowButton();
        void setFollowButtonToFollowing();
        void setFollowButtonToFollow();
        void enableFollowButton();
        void disableFollowButton();
    }

    public void showStatusDialogFragment(FragmentManager supportFragmentManager){
        StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
        statusDialogFragment.show(supportFragmentManager, "post-status-dialog");
    }



    public void updateSelectedUserFollowingAndFollowers(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        var userService = new UserService();
        userService.updateFollowingAndFollowers(currUserAuthToken,
                selectedUser, this);
    }
    @Override
    public void getFollowersCountSuccess(int followersCount) {
        view.setFollowersCount(followersCount);
    }

    @Override
    public void getFollowersCountFailure(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void getFolloweesCountSuccess(int followeesCount) {
        view.setFolloweesCount(followeesCount);
    }

    @Override
    public void getFolloweesCountFailure(String message) {
        view.showErrorMessage(message);
    }



    public void checkIsFollower(User selectedUser, User currentUser, AuthToken currUserAuthToken){

        if (selectedUser.compareTo(currentUser) == 0){
            view.hideFollowButton();
        } else {
            view.showFollowButton();

            var followService = new FollowService();
            followService.isFollower(currUserAuthToken, currentUser, selectedUser, this);
        }
    }

    @Override
    public void isFollowerSucceeded(boolean isFollower){
        // If logged in user if a follower of the selected user, display the follow button as "following"
        if (isFollower) {
            view.setFollowButtonToFollowing();
        } else {
            view.setFollowButtonToFollow();
        }
    }

    @Override
    public void isFollowerFailed(String message){
        view.showErrorMessage(message);
    }



    public void followButtonClick(String followingString, String followButtonText,
                                  AuthToken currUserAuthToken, User currUser, User selectedUser){
        view.disableFollowButton();

        if(followButtonText.equals(followingString)) {
            // already following, so we UNFOLLOW
            var unfollowService = new FollowService();
            unfollowService.unfollow(currUserAuthToken, selectedUser, currUser, this);
        } else {
            // not current following, so we FOLLOW
            var followService = new FollowService();
            followService.follow(currUserAuthToken, selectedUser, currUser, this);
        }
    }

    @Override
    public void startingFollow(User selectedUser) {
        view.showInfoMessage("Adding " + selectedUser.getName() + "...");
    }
    @Override
    public void endingFollow() {
        view.hideInfoMessage();
    }

    @Override
    public void followSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        updateSelectedUserFollowingAndFollowers(currUserAuthToken, currUser, selectedUser);
        updateFollowButton(false);
    }

    @Override
    public void followFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void startingUnfollow(User selectedUser) {
        view.showInfoMessage("Removing " + selectedUser.getName() + "...");
    }

    @Override
    public void endingUnfollow(){
        view.hideInfoMessage();
    }
    @Override
    public void unfollowSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        updateSelectedUserFollowingAndFollowers(currUserAuthToken, currUser, selectedUser);
        updateFollowButton(true);
    }

    @Override
    public void unfollowFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void enableFollowButton(){
        view.enableFollowButton();
    }

    private void updateFollowButton(boolean removed) {
        // If follow relationship was removed.
        if (removed) {
            view.setFollowButtonToFollow();
        } else {
            view.setFollowButtonToFollowing();
        }
    }
}
