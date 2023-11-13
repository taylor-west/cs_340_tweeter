package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user's feed.
 */
public class GetFeedRequest {

    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private Status lastStatus;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFeedRequest() {}

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose story/statuses  are to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last status that was returned in the previous request (null if
     *                     there was no previous request or if no statuses were returned in the
     *                     previous request).
     */
    public GetFeedRequest(AuthToken authToken, String userAlias, int limit, Status lastStatus) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the alias of the user whose feed is be returned by this request.
     *
     * @return the alias of the user whose feed is being retrieved.
     */
    public String getUserAlias() {
        return userAlias;
    }

    /**
     * Sets the follower.
     *
     * @param userAlias the alias of the user whose feed is being retrieved.
     */
    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    /**
     * Returns the number of statuses that will be included with this request (pageSize)
     * @return the limit (pageSize).
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the number of statuses that will be returned by this request (pageSize)
     *
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last that was returned by the previous page/request.
     *
     * @return the status.
     */
    public Status getLastStatus() {
        return lastStatus;
    }

    /**
     * Sets the lastStatus.
     *
     * @param lastStatus the last status.
     */
    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }


    @Override
    public String toString() {
        return "GetFeedRequest{" +
                "authToken=" + getAuthToken() +
                ", userAlias='" + getUserAlias() + '\'' +
                ", limit=" + limit +
                '}';
    }
}
