package edu.byu.cs.tweeter.client.presenter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements UserService.GetUserObserver, StatusService.GetFeedObserver {
    private static final int PAGE_SIZE = 10;

    public FeedPresenter(View view, User user) {
        this.view = view;
        this.user = user;
    }

    @Override
    public void getFeedSucceeded(List<Status> statuses, boolean hasMorePages) {
        isLoading = false;
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.endingLoading();
        view.addItems(statuses);
    }

    @Override
    public void getFeedFailed(String message) {
        view.showErrorMessage(message);
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

    public void getUser(AuthToken authToken, String alias) {
        var userService = new UserService();
        userService.getUser(authToken, alias,this);
        view.showInfoMessage("Getting user's profile...");
    }

    public void clickedLink(AuthToken authToken, TextView clickedMention){
        Spanned s = (Spanned) clickedMention.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);

        String clickable = s.subSequence(start, end).toString();

        getUser(authToken, clickable);
    }

    public void setUrls(Status status, SpannableString spannableString){
        for (String url : status.getUrls()) {
                int startIndex = status.getPost().indexOf(url);
                spannableString.setSpan(new URLSpan(url), startIndex, (startIndex + url.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
    }

    /**
     * Causes the Adapter to display a loading footer and make a request to get more feed
     * data.
     */
    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.startingLoading();

            var statusService = new StatusService();
            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(),
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
