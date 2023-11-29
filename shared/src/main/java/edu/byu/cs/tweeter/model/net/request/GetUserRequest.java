package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return information
 * about the specified user.
 */
public class GetUserRequest extends AuthenticatedRequest{
    private String targetUserAlias

;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetUserRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param currUserAlias the alias of the current user (whose is logged in and making the request)
     * @param targetUserAlias the alias of the user whose story/statuses  are to be returned.
     */
    public GetUserRequest(AuthToken authToken, String currUserAlias, String targetUserAlias) {
        super(authToken, currUserAlias);

        this.targetUserAlias = targetUserAlias;
    }

    /**
     * Returns the target user (user whose information is to be returned by this request).
     *
     * @return the target user.
     */
    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    /**
     * Sets the target user (user whose information is to be returned by this request).
     *
     * @param targetUserAlias the target user.
     */
    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }

    @Override
    public String toString() {
        return "GetUserRequest{" +
                "authToken=" + getAuthToken() +
                "currUserAlias=" + getCurrUserAlias() +
                ", targetUserAlias='" + getTargetUserAlias() + '\'' +
                '}';
    }
}
