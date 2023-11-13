package edu.byu.cs.tweeter.model.net.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest{
    private AuthToken authToken;
    private String userAlias;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the user who is posting the status.
     * @param userAlias the alias of the user who is posting the status.
     * @param status the status that the user will post.
     */
    public PostStatusRequest(AuthToken authToken, String userAlias, Status status) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.status = status;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostStatusRequest that = (PostStatusRequest) o;
        return Objects.equals(userAlias, that.userAlias) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAlias, status);
    }

    @Override
    public String toString() {
        return "PostStatusRequest{" +
                "userAlias='" + userAlias + '\'' +
                ", status=" + status.hashCode() +
                '}';
    }
}
