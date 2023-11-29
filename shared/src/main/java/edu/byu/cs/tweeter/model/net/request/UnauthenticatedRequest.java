package edu.byu.cs.tweeter.model.net.request;

public abstract class UnauthenticatedRequest {
    protected final static int DEFAULT_AUTHTOKEN_LIFESPAN_IN_SECONDS = (15 * 60); // 15 minutes
    private String username;
    private String password;

    private int desiredAuthTokenLifespan;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    protected UnauthenticatedRequest() {}

    /**
     * Creates an Instance (only used for inheritance).
     *
     * @param username the alleged username of the user attempting the request
     * @param password the alleged password of the user attempting the request
     */
    public UnauthenticatedRequest(String username, String password, Integer desiredAuthTokenLifespan) {
        this.username = username;
        this.password = password;
        this.desiredAuthTokenLifespan = desiredAuthTokenLifespan;
    }


    /**
     * Returns the alleged username of the user making the request
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the alleged password of the user making the request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the desired amount of time (in seconds) that the returned AuthToken will be valid for if the authentication request is successful.
     * @return the desiredAuthTokenLifespan
     */
    public int getDesiredAuthTokenLifespan() {
        return desiredAuthTokenLifespan;
    }

    /**
     * Sets the desired amount of time (in seconds) that the returned AuthToken will be valid for if the authentication request is successful.
     * @param desiredAuthTokenLifespan the number of seconds the AuthToken should be good for
     */
    public void setDesiredAuthTokenLifespan(int desiredAuthTokenLifespan) {
        this.desiredAuthTokenLifespan = desiredAuthTokenLifespan;
    }
}
