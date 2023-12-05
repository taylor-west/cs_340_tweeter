package edu.byu.cs.tweeter.model.net.request;
import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowersRequest extends PaginatedRequest<String> {

    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowersRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param limit the maximum number of followers to return.
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous request (null if
     *                     there was no previous request or if no followers were returned in the
     *                     previous request).
     * @param followeeAlias the alias of the user whose followers are to be returned.
     */
    public FollowersRequest(AuthToken authToken, int limit, String lastFollowerAlias, String followeeAlias) {
        super(authToken, limit, lastFollowerAlias);

        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    /**
     * Sets the follower.
     *
     * @param followeeAlias the follower.
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    @Override
    public String toString() {
        return "FollowersRequest{" +
                "authToken=" + getAuthToken() +
                ", followeeAlias='" + followeeAlias + '\'' +
                ", limit=" + getLimit() +
                ", lastItem (follower alias)='" + getLastItem() + '\'' +
                '}';
    }
}
