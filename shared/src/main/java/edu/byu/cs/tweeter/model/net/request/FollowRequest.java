package edu.byu.cs.tweeter.model.net.request;

public class FollowRequest {
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param followerAlias the alias of the follower (user that is going to be following the followee)
     * @param followeeAlias the alias of the followee (user that is going to be followed by the follower)
     */
    public FollowRequest(String followerAlias, String followeeAlias) {
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
                "followerAlias=" + followerAlias +
                ", followeeAlias=" + followeeAlias +
                '}';
    }
}
