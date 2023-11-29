package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user's feed.
 */
public class GetFeedRequest extends PaginatedRequest<Status> {
    private String targetUserAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFeedRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (should be the follower)
     * @param currUserAlias the alias of the current user (logged in performing the request)
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last status that was returned in the previous request (null if
     *                     there was no previous request or if no statuses were returned in the
     *                     previous request).
     * @param targetUserAlias the alias of the user whose story/statuses  are to be returned.
     */
    public GetFeedRequest(AuthToken authToken, String currUserAlias, int limit, Status lastStatus, String targetUserAlias) {
        super(authToken, currUserAlias, limit, lastStatus);

        this.targetUserAlias = targetUserAlias;
    }

    /**
     * Returns the alias of the user whose feed is be returned by this request.
     *
     * @return the alias of the user whose feed is being retrieved.
     */
    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    /**
     * Sets the target user alias.
     *
     * @param targetUserAlias the alias of the user whose feed is being retrieved.
     */
    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }

    @Override
    public String toString() {
        return "GetFeedRequest{" +
                "authToken=" + getAuthToken() +
                ", currUserAlias=" + getCurrUserAlias() +
                ", targetUserAlias='" + getTargetUserAlias() + '\'' +
                ", limit=" + getLimit() +
                ", lastItem (status)=" + getLastItem() +
                '}';
    }
}
