package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedRequest {
    private AuthToken authToken;
    private String currUserAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    protected AuthenticatedRequest() {}

    /**
     * Creates an Instance (only used for inheritance).
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param currUserAlias the alias of the current user (whose is logged in and making the request)
     */
    public AuthenticatedRequest(AuthToken authToken, String currUserAlias) {
        this.authToken = authToken;
        this.currUserAlias = currUserAlias;
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

    /**
     * Gets the alias of the user that is currently logged in (making the request)
     *
     * @return the alias of the current User.
     */
    public String getCurrUserAlias() {
        return currUserAlias;
    }

    /**
     * Sets the alias of the user that is currently logged in (making the request)
     *
     * @param currUserAlias the alias of the current User.
     */
    public void setCurrUserAlias(String currUserAlias) {
        this.currUserAlias = currUserAlias;
    }
}
