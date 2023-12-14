package edu.byu.cs.tweeter.model.net.response;


import edu.byu.cs.tweeter.model.net.request.UpdateFeedsRequest;

/**
 * A response for a {@link UpdateFeedsRequest}.
 */
public class UpdateFeedsResponse extends Response{
    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UpdateFeedsResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     */
    public UpdateFeedsResponse() {
        super(true, null);
    }
}
