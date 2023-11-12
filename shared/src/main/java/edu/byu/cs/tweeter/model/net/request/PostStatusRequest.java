package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest{
    private String userAlias;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {}

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user who is posting the status.
     * @param status the status that the user will post.
     */
    public PostStatusRequest(String userAlias, Status status) {
        this.userAlias = userAlias;
        this.status = status;
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


}
