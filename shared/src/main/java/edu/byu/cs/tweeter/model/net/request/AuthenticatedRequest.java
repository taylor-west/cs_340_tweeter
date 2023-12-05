package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedRequest {
    private AuthToken authToken;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    protected AuthenticatedRequest() {}

    /**
     * Creates an Instance (only used for inheritance).
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     */
    public AuthenticatedRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the authToken.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the authToken.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
