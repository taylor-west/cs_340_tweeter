package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerRequest extends AuthenticatedRequest{
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param currUserAlias the alias of the current user (whose is logged in and making the request)
     * @param followerAlias the alias of the follower (user that is going to be following the followee)
     * @param followeeAlias the alias of the followee (user that is going to be followed by the follower)
     */
    public IsFollowerRequest(AuthToken authToken, String currUserAlias, String followerAlias, String followeeAlias) {
        super(authToken, currUserAlias);

        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns alias/username follower (user that is following the followee)
     *
     * @return the follower (user that is following the followee).
     */
    public String getFollowerAlias() {
        return followerAlias;
    }

    /**
     * Sets the followerAlias.
     *
     * @param followerAlias the alias/username of the follower (user that is following the followee)
     */
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    /**
     * Returns followeeAlias (alias of the user that is being followed by the follower)
     *
     * @return the alias of the followee (user that is being followed by the follower).
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    /**
     * Sets the followee alias.
     *
     * @param followeeAlias the alias of the followee (user that is being followed by the follower
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    @Override
    public String toString() {
        return "FollowRequest{" +
                "authToken=" + getAuthToken() +
                ", currUserAlias=" + getCurrUserAlias() +
                ", followerAlias=" + getFollowerAlias() +
                ", followeeAlias=" + getFolloweeAlias() +
                '}';
    }
}
