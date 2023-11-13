package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;

/**
 * A paged response for a {@link GetFollowersCountRequest}.
 */
public class GetFollowersCountResponse extends Response {

    private int followersCount;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetFollowersCountResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param followersCount    the number of followees (users that the specified user/follower is following).
     */
    public GetFollowersCountResponse(int followersCount) {
        super(true);
        this.followersCount = followersCount;

        System.out.println("new GetFollowersCountResponse is being constructed with followersCount: " + followersCount);

    }
    
    /**
     * Returns the number of followers for user specified by the request.
     *
     * @return the number of followers.
     */
    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetFollowersCountResponse that = (GetFollowersCountResponse) o;
        return followersCount == that.followersCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(followersCount);
    }

    @Override
    public String toString() {
        return "GetFollowingCountResponse{" +
                "followersCount=" + followersCount +
                '}';
    }
}
