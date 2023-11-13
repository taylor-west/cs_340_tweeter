package edu.byu.cs.tweeter.model.net.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest {
    private AuthToken authToken;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private LogoutRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the authToken of the user to be logged out
     */
    public LogoutRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns authToken of the user to be logged out
     *
     * @return the authToken
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the authToken of the user to be logged out.
     *
     * @param authToken the uthToken of the user to be logged out.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogoutRequest that = (LogoutRequest) o;
        return Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken);
    }

    @Override
    public String toString() {
        return "LogoutRequest{" +
                "authToken=" + authToken.toString() +
                '}';
    }
}
