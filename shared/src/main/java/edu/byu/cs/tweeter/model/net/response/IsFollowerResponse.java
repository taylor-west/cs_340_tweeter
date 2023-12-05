package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;

/**
 * A response for a {@link IsFollowerRequest}.
 */
public class IsFollowerResponse extends Response {
    private boolean isFollower;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IsFollowerResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     */
    public IsFollowerResponse(Boolean isFollower) {
        super(true, null);
        this.isFollower = isFollower;
    }

    public boolean getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(boolean isFollower) {
        this.isFollower = isFollower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsFollowerResponse that = (IsFollowerResponse) o;
        return isFollower == that.isFollower;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFollower);
    }

    @Override
    public String toString() {
        return "IsFollowerResponse{" +
                "currentlyAFollower=" + isFollower +
                '}';
    }
}
