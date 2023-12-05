package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class GetStoryRequest extends PaginatedRequest<Status> {
    private User targetUser;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetStoryRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param targetUser the user whose story this request will retrieve
     * @param limit the maximum number of items to return at a time (pageSize)
     * @param lastStatus the last status that was returned in the previous request (null if
     *                     there was no previous request or if no statuses were returned in the
     *                     previous request).
     */
    public GetStoryRequest(AuthToken authToken, User targetUser, int limit, Status lastStatus) {
        super(authToken, limit, lastStatus);

        this.targetUser = targetUser;
    }

    /**
     * Returns the target user whose story is to be returned by this request.
     *
     * @return the target user.
     */
    public User getTargetUser() {
        return targetUser;
    }

    /**
     * Sets the target user.
     *
     * @param targetUser the user whose story is to be returned by this request.
     */
    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    /**
     * Returns the alias of the target user whose story is to be returned by this request.
     *
     * @return the target user's alias.
     */
    public String getTargetUserAlias() {
        return targetUser.getAlias();
    }

    /**
     * Sets the target user's alias.
     *
     * @param targetUserAlias the alias of the user whose story is to be returned by this request.
     */
    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUser.setAlias(targetUserAlias);
    }
}
