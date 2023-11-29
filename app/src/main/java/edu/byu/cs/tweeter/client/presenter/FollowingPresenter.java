package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The presenter for the "following"/followees functionality of the application.
 */
public class FollowingPresenter extends PagedPresenter<User> implements PagedObserver<User>{

    private static final String LOG_TAG = "FollowingPresenter";
    public static final int PAGE_SIZE = 10;

//    private final View view;
//    private final User user;
//    private final AuthToken authToken;
//
//    private User lastFollowee;
//    private boolean hasMorePages = true;
//    private boolean isLoading = false;

    private FollowService followService;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface FollowingView extends PagedView<User> {
    }
//    public interface View {
//        void setLoading(boolean value);
//        void addItems(List<User> newUsers);
//        void displayErrorMessage(String message);
//    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     * @param targetUser the user that is currently logged in.
     * @param authToken the auth token for the current session.
     */
    public FollowingPresenter(View view, User targetUser, AuthToken authToken) {
        // An assertion would be better, but Android doesn't support Java assertions
        if(view == null) {
            throw new NullPointerException();
        }
        this.view = view;
        this.targetUser = targetUser;
        this.authToken = authToken;
    }

//    public User getLastFollowee() {
//        return lastFollowee;
//    }
//
//    private void setLastFollowee(User lastFollowee) {
//        this.lastFollowee = lastFollowee;
//    }
//
//    public boolean isHasMorePages() {
//        return hasMorePages;
//    }
//
//    private void setHasMorePages(boolean hasMorePages) {
//        this.hasMorePages = hasMorePages;
//    }
//
//    public boolean isLoading() {
//        return isLoading;
//    }
//
//    private void setLoading(boolean loading) {
//        isLoading = loading;
//    }

//    /**
//     * Called by the view to request that another page of "following" users be loaded.
//     */
//    public void loadMoreItems() {
//        if (!isLoading && hasMorePages) {
//            setLoading(true);
//            view.setLoading(true);
//
//            getFollowing(authToken, user, PAGE_SIZE, lastFollowee);
//        }
//    }

    /**
     * Requests the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned for a previous request. This is an asynchronous
     * operation.
     *
     * @param authToken the session auth token.
     * @param currUser the user that is currently logged in
     * @param targetUser the user for whom followees are being retrieved.
     * @param pageSize the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    public void getItems(AuthToken authToken, User currUser, User targetUser, int pageSize, User lastFollowee) {
        getFollowingService().getFollowing(authToken, currUser, targetUser, pageSize, lastFollowee, this);
    }

    /**
     * Returns an instance of {@link FollowService}. Allows mocking of the FollowService class
     * for testing purposes. All usages of FollowService should get their FollowService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public FollowService getFollowingService() {
        if(followService == null) {
            followService = new FollowService();
        }

        return followService;
    }

//    /**
//     * Adds new followees retrieved asynchronously from the service to the view.
//     *
//     * @param followees the retrieved followees.
//     * @param hasMorePages whether or not there are more followees to retrieve.
//     */
//    @Override
//    public void handleSuccess(List<User> followees, boolean hasMorePages) {
//        setLastFollowee((followees.size() > 0) ? followees.get(followees.size() - 1) : null);
//        setHasMorePages(hasMorePages);
//
//        view.setLoading(false);
//        view.addItems(followees);
//        setLoading(false);
//    }

//    /**
//     * Notifies the presenter when asynchronous retrieval of followees failed.
//     *
//     * @param message error message.
//     */
//    @Override
//    public void handleFailure(String message) {
//        String errorMessage = "Failed to retrieve followees: " + message;
//        Log.e(LOG_TAG, errorMessage);
//
//        view.setLoading(false);
//        view.displayErrorMessage(errorMessage);
//        setLoading(false);
//    }
//
//    /**
//     * Notifies the presenter that an exception occurred in an asynchronous method this class is
//     * observing.
//     *
//     * @param exception the exception.
//     */
//    @Override
//    public void handleException(Exception exception) {
//        String errorMessage = "Failed to retrieve followees because of exception: " + exception.getMessage();
//        Log.e(LOG_TAG, errorMessage, exception);
//
//        view.setLoading(false);
//        view.displayErrorMessage(errorMessage);
//        setLoading(false);
//    }
}