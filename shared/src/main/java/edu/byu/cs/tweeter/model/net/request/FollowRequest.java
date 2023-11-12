package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;


public class FollowRequest {
    private User follower;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param follower the user that is going to be following the followee
     * @param followee the user that is going to be followed by the follower
     */
    public FollowRequest(User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns follower (user that is following the followee)
     *
     * @return the follower (user that is following the followee).
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     *
     * @param follower the follower (user that following the followee
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Returns followee (user that is being followed by the follower)
     *
     * @return the followee (user that is being followed by the follower).
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the followee.
     *
     * @param followee the followee (user that is being followed by the follower
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }
}
