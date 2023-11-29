package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowingRequest extends PaginatedRequest<String>{
    private String followerAlias;


    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowingRequest() {}

    /**
     * Creates an instance.
     *
     * @param followerAlias the alias of the user whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowingRequest(AuthToken authToken, String currUserAlias, int limit, String lastFolloweeAlias, String followerAlias) {
        super(authToken, currUserAlias, limit, lastFolloweeAlias);

        this.followerAlias = followerAlias;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFollowerAlias() {
        return followerAlias;
    }

    /**
     * Sets the follower.
     *
     * @param followerAlias the follower.
     */
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    @Override
    public String toString() {
        return "FollowingRequest{" +
                "followerAlias='" + followerAlias + '\'' +
                ", limit=" + getLimit() +
                ", lastItem (followee alias)='" + lastItem + '\'' +
                '}';
    }
}
