package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;

/**
 * A response for a {@link FollowRequest}.
 */
public class FollowResponse extends Response {
    private User follower;
    private User followee;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param follower the user that is now following the followee
     * @param followee the user that is now being followed bvy the follower
     */
    public FollowResponse(User follower, User followee) {
        super(true, null);
        this.follower = follower;
        this.followee= followee;
    }


    /**
     * Returns the follower (user that is following the followee).
     *
     * @return the follower (user that is following the followee).
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Returns followee (user that is being followed by the follower).
     *
     * @return the followee (user that is being followed by the follower).
     */
    public User getFollowee() {
        return followee;
    }
}
