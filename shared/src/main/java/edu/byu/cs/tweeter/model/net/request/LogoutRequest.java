package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest extends AuthenticatedRequest {

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private LogoutRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the authToken of the user to be logged out
     */
    public LogoutRequest(AuthToken authToken, String currUserAlias) {
        super(authToken, currUserAlias);
    }


    @Override
    public String toString() {
        return "LogoutRequest{" +
                "authToken=" + getAuthToken() +
                '}';
    }
}
