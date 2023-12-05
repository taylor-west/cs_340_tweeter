package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest extends AuthenticatedRequest {
    private String targetUserAlias;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the user who is posting the status.
     * @param targetUserAlias the alias of the target user (user who is posting the status).
     * @param status the status that the user will post.
     */
    public PostStatusRequest(AuthToken authToken, String targetUserAlias, Status status) {
        super(authToken);

        this.targetUserAlias = targetUserAlias;
        this.status = status;
    }


    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PostStatusRequest{" +
                "authToken=" + getAuthToken() +
                ", targetUserAlias='" + getTargetUserAlias() + '\'' +
                ", status=" + status.hashCode() +
                '}';
    }
}
