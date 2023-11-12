package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements UserService.GetUserObserver, StatusService .GetStoryObserver{
    private static final int PAGE_SIZE = 10;

    public StoryPresenter(View view, User user) {
        this.view = view;
        this.user = user;
    }

    @Override
    public void getStorySucceeded(List<Status> statuses, boolean hasMorePages) {
        this.isLoading = false;
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.endingLoading();
        view.addItems(statuses);

    }

    @Override
    public void getStoryFailed(String message) {
        view.showErrorMessage(message);
    }

    public void getUser(AuthToken authToken, String alias) {

        var userService = new UserService();
        userService.getUser(authToken, alias,this);
        view.showInfoMessage("Getting user's profile...");
    }

    @Override
    public void getUserSucceeded(User user) {
        view.openMainView(user);
    }

    @Override
    public void getUserFailed(String message) {
        view.showErrorMessage(message);
    }

    // every Presenter class should define the interface for the view
    public interface View {
        void showInfoMessage(String message);
        void showErrorMessage(String message);

        void openMainView(User user);

        void startingLoading();
        void endingLoading();

        void addItems(List<Status> statuses);
    }

    private View view;
    private User user;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public void getStory(AuthToken authToken, User user) {
        var storyService = new StatusService();
        storyService.getStory(authToken, user, PAGE_SIZE, lastStatus, this);
    }

    /**
     * Causes the Adapter to display a loading footer and make a request to get more story
     * data.
     */
    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.startingLoading();

            var storyService = new StatusService();
            storyService.getStory(Cache.getInstance().getCurrUserAuthToken(),
                    user, PAGE_SIZE, lastStatus, this);
        }
    }

    public boolean getIsLoading(){
        return isLoading;
    }

    public boolean getHasMorePages(){
        return hasMorePages;
    }
}
