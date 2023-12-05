package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends AuthenticatedRequest {
    private User follower;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (should be the follower)
     * @param follower the User that is going to be following the followee
     * @param followee the User that is going to be followed by the follower
     */
    public FollowRequest(AuthToken authToken, User follower, User followee) {
        super(authToken);

        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns the follower (User that is following the followee)
     * @return the User that is following the followee.
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     * @param follower the User that is following the followee
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Returns the followee (User that is being followed by the follower).
     * @return the User that is being followed by the follower
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the followee.
     * @param followee the User that is being followed by the followee
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }

    /**
     * Returns alias/username pf the follower (user that is following the followee)
     *
     * @return the alias of the follower (user that is following the followee).
     */
    public String getFollowerAlias() {
        return follower.getAlias();
    }

    /**
     * Sets the follower alias.
     *
     * @param followerAlias the alias/username of the follower (user that is following the followee)
     */
    public void setFollowerAlias(String followerAlias) {
        this.follower.setAlias(followerAlias);
    }

    /**
     * Returns followee's alias (alias of the user that is being followed by the follower)
     *
     * @return the alias of the followee (user that is being followed by the follower).
     */
    public String getFolloweeAlias() {
        return followee.getAlias();
    }

    /**
     * Sets the followee's alias.
     *
     * @param followeeAlias the alias of the followee (user that is being followed by the follower
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followee.setAlias(followeeAlias);
    }



    @Override
    public String toString() {
        return "FollowRequest{" +
                "authToken=" + getAuthToken() +
                ", follower=" + getFollower().toString() +
                ", followee=" + getFollowee().toString() +
                '}';
    }
}
