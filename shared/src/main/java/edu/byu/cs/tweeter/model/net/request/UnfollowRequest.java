package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowRequest extends AuthenticatedRequest{
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private UnfollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (follower)
     * @param followerAlias the alias of the follower (user that is was following the followee)
     * @param followeeAlias the alias of the followee (user that is was followed by the follower)
     */
    public UnfollowRequest(AuthToken authToken, String followerAlias, String followeeAlias) {
        super(authToken);

        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns alias/username follower (user that was following the followee)
     *
     * @return the follower (user that was following the followee).
     */
    public String getFollowerAlias() {
        return followerAlias;
    }

    /**
     * Sets the followerAlias.
     *
     * @param followerAlias the alias/username of the follower (user that was following the followee)
     */
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    /**
     * Returns followeeAlias (alias of the user that was followed by the follower)
     *
     * @return the alias of the followee (user that waw followed by the follower).
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    /**
     * Sets the followee alias.
     *
     * @param followeeAlias the alias of the followee (user that was followed by the follower
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    @Override
    public String toString() {
        return "UnfollowRequest{" +
                "authToken=" + getAuthToken() +
                ", followerAlias=" + getFollowerAlias() +
                ", followeeAlias=" + getFolloweeAlias() +
                '}';
    }
}
