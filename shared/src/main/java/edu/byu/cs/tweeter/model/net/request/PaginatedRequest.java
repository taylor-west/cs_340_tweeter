package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PaginatedRequest<T> extends AuthenticatedRequest {

    protected int limit;
    protected T lastItem;


    /**
     * Allows construction of the object from Json. Protected so that subclaseses can implement
     * their own, Private version that won't be called in normal code.
     */
    protected PaginatedRequest() {
        super();
    }

    /**
     *
     * @param authToken the AuthToken of the current user (whose is logged in and making the request)
     * @param limit the maximum number of items to return at a time (pageSize)
     * @param lastItem the last item that was returned in the previous request (null if
     *                 there was no previous request or if no items were returned in the
     *                 previous request).
     */
    protected PaginatedRequest(AuthToken authToken, int limit, T lastItem) {
        super(authToken);

        this.limit = limit;
        this.lastItem = lastItem;
    }

    /**
     * Returns the number representing the maximum number of items to be returned by a request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last item that was returned in the previous request or null if there was no
     * previous request or if no items were returned in the previous request.
     *
     * @return the last item.
     */
    public T getLastItem() {
        return lastItem;
    }

    /**
     * Sets the last item.
     *
     * @param lastItem the last item.
     */
    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }
}
