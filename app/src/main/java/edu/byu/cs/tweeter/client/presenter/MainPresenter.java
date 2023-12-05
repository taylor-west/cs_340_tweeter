// TODO: break Handlers out into separate files/folders and subclassing them (same base Folder as Tasks)
// TODO: reduce Handlers down to the number of differences in the handleSuccess
// TODO: use composition to implement observers

package edu.byu.cs.tweeter.client.presenter;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.observers.FollowObserver;
import edu.byu.cs.tweeter.client.presenter.observers.IsFollowerObserver;
import edu.byu.cs.tweeter.client.presenter.observers.LogoutObserver;
import edu.byu.cs.tweeter.client.presenter.observers.PostStatusObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UnfollowObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UpdateFolloweesAndFollowersObserver;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter implements
        LogoutObserver,
        UpdateFolloweesAndFollowersObserver,
        IsFollowerObserver,
        FollowObserver,
        UnfollowObserver,
        PostStatusObserver {
    private User currUser;

    public interface MainView extends View {
        void hideErrorMessage();

        void hideInfoMessage();

        void showFollowButton(boolean visible);

        void setFolloweesCount(int followeesCount);

        void setFollowersCount(int followersCount);

        void isFollowing(boolean following);

        void enableFollowButton(boolean enabled);

        void openLoginView();
    }

    public MainPresenter(User user, View view) {
        this.currUser = user;
        this.view = view;
    }

    //// required BasePresenter methods ////
    protected MainView getView() {
        return (MainView) view;
    }

    @Override
    public void handleFailure(String message) {
        getView().hideInfoMessage();
        getView().hideErrorMessage();
        getView().showErrorMessage(message);
    }

    @Override
    public void handleExceptions(String message) {
        getView().hideInfoMessage();
        getView().hideErrorMessage();
        getView().showErrorMessage(message);
    }
    ////  ////

    public void validateUser() {
        if (this.currUser == null) {
            getView().showErrorMessage("User not passed to activity");
        }
    }


    //// Logout ////
    // required by LogoutObserver
    public void logout(AuthToken currUserAuthToken) {
        getView().showInfoMessage("Logging Out...");

        this.userService = getUserService();
        this.userService.logout(currUserAuthToken, Cache.getInstance().getCurrUser(), this);
    }

    @Override
    public void logoutSucceeded() {
        getView().hideInfoMessage();
        getView().openLoginView();
    }
    ////////////////

    //// Update Followers and Following ////
    // required by UpdateFollowingAndFollowersObserver
    public void updateSelectedUserFollowingAndFollowers(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        this.userService = getUserService();
        this.userService.updateFollowingAndFollowers(currUserAuthToken, Cache.getInstance().getCurrUser(),
                selectedUser, this);
    }

    @Override
    public void getFollowersCountSuccess(int followersCount) {
        getView().setFollowersCount(followersCount);
    }

    @Override
    public void getFollowingCountSuccess(int followeesCount) {
        getView().setFolloweesCount(followeesCount);
    }
    ////////////////


    //// Check if IsFollower ////
    // required by IsFollowerObserver
    public void checkIsFollower(User selectedUser, User currentUser, AuthToken currUserAuthToken) {
        System.out.println("NOW IN checkIsFollower");
        if (selectedUser.compareTo(currentUser) == 0) {
            getView().showFollowButton(false);
            getView().isFollowing(false);

        } else {
            getView().showFollowButton(true);
            getView().isFollowing(true);

            FollowService followService = new FollowService();
            followService.isFollower(currUserAuthToken, currentUser, selectedUser, this);
        }
    }

    @Override
    public void isFollowerSucceeded(boolean isFollower) {
        System.out.println("NOW IN isFollowerSucceeded (isFollower=" + isFollower + ")");

        // If logged in user if a follower of the selected user, display the follow button as "following"
        getView().isFollowing(isFollower);
    }
    ////////////////

    //// Follow/Unfollow Button ////
    public void followButtonClick(String followingString, String followButtonText,
                                  AuthToken currUserAuthToken, User currUser, User selectedUser) {
        getView().enableFollowButton(false);
        FollowService followService = new FollowService();

        if (followButtonText.equals(followingString)) {
            // already following, so we UNFOLLOW
            followService.unfollow(currUserAuthToken, currUser, selectedUser, this);
        } else {
            // not current following, so we FOLLOW
            followService.follow(currUserAuthToken, currUser, selectedUser, this);
        }
    }
    ////////////////

    //// Follow ////
    // required by FollowObserver

    /**
     * Informs the View that a Follow action is taking place (displays infoMessage).
     * Required by FollowObserver. Used in the Follow/Unfollow flow.
     *
     * @param selectedUser
     */
    @Override
    public void startingFollow(User selectedUser) {
        getView().showInfoMessage("Adding " + selectedUser.getName() + "...");
    }

    /**
     * Informs the View that the Follow action has completed (hides infoMessage).
     * Required by FollowObserver. Used in the Follow/Unfollow flow.
     */
    @Override
    public void endingFollow() {
        getView().hideInfoMessage();
    }

    /**
     * Updates the user's follower/following count, updates the FollowButton, and hides the loading message.
     * Required by FollowObserver. Used in the Follow/Unfollow flow.
     *
     * @param currUserAuthToken
     * @param currUser
     * @param selectedUser
     */
    @Override
    public void followSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        endingFollow();
        updateSelectedUserFollowingAndFollowers(currUserAuthToken, currUser, selectedUser);
        getView().isFollowing(true);
        getView().enableFollowButton(true);
    }
    ////////////////

    //// Unfollow ////
    // required by UnfollowObserver

    /**
     * Informs the View that an Unfollow action is taking place (displays infoMessage).
     * Required by UnfollowObserver. Used in the Follow/Unfollow flow.
     *
     * @param selectedUser
     */
    @Override
    public void startingUnfollow(User selectedUser) {
        getView().showInfoMessage("Removing " + selectedUser.getName() + "...");
    }

    /**
     * Informs the View that the Unfollow action has completed (hides infoMessage).
     * Required by UnfollowObserver. Used in the Follow/Unfollow flow.
     */
    @Override
    public void endingUnfollow() {
        getView().hideInfoMessage();
    }

    /**
     * Updates the user's follower/following count, updates the FollowButton, and hides the loading message.
     * Required by UnfollowObserver. Used in the Follow/Unfollow flow.
     *
     * @param currUserAuthToken
     * @param currUser
     * @param selectedUser
     */
    @Override
    public void unfollowSucceeded(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        endingUnfollow();
        updateSelectedUserFollowingAndFollowers(currUserAuthToken, currUser, selectedUser);
        getView().isFollowing(false);
        getView().enableFollowButton(true);
    }
    ////////////////

    //// Post Status ////
    // required by PostStatusObserver

    /**
     * Main function used by presenter for the Post Status flow. See PostStatusObserver and PostStatusHandler.
     *
     * @param post
     * @param currUser
     * @param currUserAuthToken
     */
    public void onStatusPosted(String post, User currUser, AuthToken currUserAuthToken) {
        getView().showInfoMessage("Posting Status...");

        Status newStatus = getStatus(post, currUser, parseURLs(post), parseMentions(post));

        this.statusService = getStatusService();
        this.statusService.postStatus(currUserAuthToken, Cache.getInstance().getCurrUser(), currUser, newStatus, this);
    }

    /**
     * Returns a Status with the appropriate attributes. The timestamp is automatically generated at the time of creation. This function is broken out to allow for easier mocking and unit testing.
     *
     * @param post
     * @param user
     * @param urls
     * @param mentions
     * @return
     */
    protected Status getStatus(String post, User user, List<String> urls, List<String> mentions) {
        return new Status(post, user, System.currentTimeMillis(), urls, mentions);
    }

    /**
     * required by PostStatusObserver
     */
    @Override
    public void postStatusSucceeded() {
        getView().hideInfoMessage();
        getView().showInfoMessage("Successfully Posted!");
    }

    /**
     * Used in the Post Status flow to extract clickable URLS
     *
     * @param post
     * @return
     */
    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    /**
     * Used in the Post Status flow to extract clickable Mentions
     *
     * @param post
     * @return
     */
    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    /**
     * Used by parseUrls() in the Post Status flow
     *
     * @param word
     * @return
     */
    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }
    ////////////////

    //// Show StatusDialogFragment ///
    // required by StatusDialogFragment.Observer

    /**
     * Used in the Post Status flow. Displays the popup dialog box for creating a new status post. See StatusDialogFragment.Observer and PostStatusHandler.
     *
     * @param supportFragmentManager
     */
    public void showStatusDialogFragment(FragmentManager supportFragmentManager) {
        StatusDialogFragment statusDialogFragment = getStatusDialogFragment();
        statusDialogFragment.show(supportFragmentManager, "post-status-dialog");
    }

    /**
     * Used in the Post Status flow. Is broken out to allow for easier mocking and unit testing.
     *
     * @return StatusDialogFragment
     */
    protected StatusDialogFragment getStatusDialogFragment() {
        return new StatusDialogFragment();
    }
    ////////////////
}
