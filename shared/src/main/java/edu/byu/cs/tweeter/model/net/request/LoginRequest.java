package edu.byu.cs.tweeter.model.net.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest extends UnauthenticatedRequest{

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private LoginRequest() {}

    /**
     * Creates an instance.
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     * @param desiredAuthTokenLifespan the number of seconds that the authToken should be valid for if the request succeeds (defaults to 30 minutes)
     */
    public LoginRequest(String username, String password, Integer desiredAuthTokenLifespan) {
        super(username, password, DEFAULT_AUTHTOKEN_LIFESPAN_IN_SECONDS);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", desiredAuthTokenLifespan=" + desiredAuthTokenLifespan +
                '}';
    }
}
