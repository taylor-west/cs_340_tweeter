package edu.byu.cs.tweeter.model.net.response;


import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;

/**
 * A response for a {@link PostStatusRequest}.
 */
public class PostStatusResponse extends Response{
    private String userAlias;
    private Status status;


    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostStatusResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param userAlias the user who posted the status.
     * @param status the status that was posted by the user.
     */
    public PostStatusResponse(String userAlias, Status status) {
        super(true, null);
        this.userAlias = userAlias;
        this.status = status;
    }

    /**
     * Returns the alias of the user who posted the status.
     *
     * @return the user alias.
     */
    public String getUserAlias() {
        return userAlias;
    }

    /**
     * Returns the status posted by the user.
     *
     * @return the status.
     */
    public Status getStatus() {
        return status;
    }
}
