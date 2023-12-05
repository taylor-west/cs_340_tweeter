package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the number of
 * followees for a specified follower.
 */
public class GetFollowersCountRequest extends AuthenticatedRequest {

    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowersCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (should be the follower)
     * @param followeeAlias the alias of the user whose followers are being counted.
     */
    public GetFollowersCountRequest(AuthToken authToken, String followeeAlias) {
        super(authToken);

        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns the followee whose followers are to be counted by this request.
     *
     * @return the followee.
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    /**
     * Sets the followee.
     *
     * @param followeeAlias the followee.
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }


    @Override
    public String toString() {
        return "GetFollowingCountRequest{" +
                "authToken=" + getAuthToken() +
                ", followeeAlias='" + getFolloweeAlias() + '\'' +
                '}';
    }
}
