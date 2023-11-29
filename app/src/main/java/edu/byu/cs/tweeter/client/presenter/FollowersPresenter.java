package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> implements PagedObserver<User> {
    private static final String LOG_TAG = "FollowingPresenter";
    public static final int PAGE_SIZE = 10;

    private FollowService followService;

    public interface FollowersView extends PagedView<User> {
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     * @param targetUser the user that is currently logged in.
     * @param authToken the auth token for the current session.
     */
    public FollowersPresenter(View view, User targetUser, AuthToken authToken) {
        this.view = view;
        this.targetUser = targetUser;
        this.authToken = authToken;
    }

    /**
     * Requests the users that are following the user specified in the request. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned for a previous request. This is an asynchronous
     * operation.
     *
     * @param authToken the session auth token.
     * @param targetUser the user for whom followees are being retrieved.
     * @param pageSize the maximum number of followees to return.
     * @param lastFollower the last followee returned in the previous request (can be null).
     */
    protected void getItems(AuthToken authToken, User currUser, User targetUser, int pageSize, User lastFollower) {
        getFollowersService().getFollowers(authToken, currUser, targetUser, pageSize, lastFollower, this);
    }

    /**
     * Returns an instance of {@link FollowService}. Allows mocking of the FollowService class
     * for testing purposes. All usages of FollowService should get their FollowService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public FollowService getFollowersService() {
        if(followService == null) {
            followService = new FollowService();
        }

        return followService;
    }
}
