package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;

/**
 * A paged response for a {@link GetFollowingCountRequest}.
 */
public class GetFollowingCountResponse extends Response {

    private int followeesCount;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetFollowingCountResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param followeesCount    the number of followees (users that the specified user/follower is following).
     */
    public GetFollowingCountResponse(int followeesCount) {
        super(true);
        this.followeesCount = followeesCount;

        System.out.println("new GetFollowingCountResponse is being constructed with followeesCount: " + followeesCount);

    }

    /**
     * Returns the number of followees for user specified by the request.
     *
     * @return the number of followees.
     */
    public int getFolloweesCount() {
        return followeesCount;
    }

    public void setFolloweesCount(int followeesCount) {
        this.followeesCount = followeesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetFollowingCountResponse that = (GetFollowingCountResponse) o;
        return followeesCount == that.followeesCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(followeesCount);
    }

    @Override
    public String toString() {
        return "GetFollowingCountResponse{" +
                "followeesCount=" + followeesCount +
                '}';
    }
}
