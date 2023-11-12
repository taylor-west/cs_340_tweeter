package edu.byu.cs.tweeter.client.presenter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observers.GetUserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BasePresenter implements GetUserObserver {
    protected static final int PAGE_SIZE = 10;
    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;

    public interface PagedView<T> extends View {
        void setLoading(boolean isLoading);

        void addItems(List<T> items);

        void navigateToUser(User user);
    }

    protected abstract void getItems(AuthToken authToken, User targerUser, int pageSize, T lastItem);


    protected PagedView<T> getView() {
        return (PagedView<T>) view;
    }

    public boolean getIsLoading() {
        return isLoading;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getView().setLoading(true);
            getItems(authToken, targetUser, PAGE_SIZE, lastItem);
        }
    }

    public void setUrls(Status status, SpannableString spannableString) {
        for (String url : status.getUrls()) {
            int startIndex = status.getPost().indexOf(url);
            spannableString.setSpan(new URLSpan(url), startIndex, (startIndex + url.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void getUser(AuthToken authToken, String alias) {
        this.isLoading = true;
        view.showInfoMessage("Getting user's profile...");

        var userService = new UserService();
        userService.getUser(authToken, alias, this);
    }

    public void getUserSucceeded(User user) {
        this.isLoading = false;
        getView().setLoading(false);
        getView().navigateToUser(user);
    }

    /**
     * Adds new items retrieved asynchronously from the service to the view.
     *
     * @param items the retrieved items.
     * @param hasMorePages whether or not there are more items to retrieve.
     */
    public void getSucceeded(List<T> items, boolean hasMorePages) {
        this.lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        this.isLoading = false;
        getView().setLoading(false);
        getView().addItems(items);
    }

    /**
     * Notifies the presenter when asynchronous retrieval of items failed.
     *
     * @param message error message.
     */
    public void handleFailure(String message) {
        this.isLoading = false;
        getView().setLoading(false);
        getView().showErrorMessage(message);
    }

    public void handleExceptions(String message) {
        this.isLoading = false;
        getView().setLoading(false);
        getView().showErrorMessage(message);
    }
}