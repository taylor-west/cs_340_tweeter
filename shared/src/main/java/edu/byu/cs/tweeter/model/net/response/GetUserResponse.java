package edu.byu.cs.tweeter.model.net.response;


import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;

/**
 * A paged response for a {@link GetStoryRequest}.
 */
public class GetUserResponse extends Response {

    private User user;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetUserResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param user    the user to be included in the result.
     */
    public GetUserResponse(User user) {
        super(true);
        this.user = user;

        System.out.println("new GetUserResponse is being constructed with user: " + user.toString());

    }

    /**
     * Returns the user for the corresponding request.
     *
     * @return the User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user for the request.
     *
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GetUserResponse{" +
                "user=" + user.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetUserResponse that = (GetUserResponse) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
