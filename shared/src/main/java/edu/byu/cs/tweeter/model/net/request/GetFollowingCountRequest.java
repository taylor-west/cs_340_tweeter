package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the number of
 * followees for a specified follower.
 */
public class GetFollowingCountRequest extends AuthenticatedRequest {

    private String followerAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowingCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the AuthToken of the current user (should be the follower)
     * @param currUserAlias the alias of the current user (logged in performing the request)
     * @param followerAlias the alias of the user whose followers are being counted.
     */
    public GetFollowingCountRequest(AuthToken authToken, String currUserAlias, String followerAlias) {
        super(authToken, currUserAlias);

        this.followerAlias = followerAlias;
    }

    /**
     * Returns the follower whose followees are to be counted by this request.
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
        return "GetFollowingCountRequest{" +
                "authToken=" + getAuthToken() +
                ", currUserAlias=" + getCurrUserAlias() +
                ", followerAlias='" + getFollowerAlias() + '\'' +
                '}';
    }
}
//
//    private String followerAlias;
//
//    /**
//     * Allows construction of the object from Json. Private so it won't be called in normal code.
//     */
//    private GetFollowingCountRequest() {}
//
//    /**
//     * Creates an instance.
//     *
//     * @param followerAlias the alias of the user whose followees are being counted.
//     */
//    public GetFollowingCountRequest(AuthToken authToken, String followerAlias) {
//        this.authToken = authToken;
//        this.followerAlias = followerAlias;
//    }
//
//    /**
//     * Returns the auth token of the user who is making the request.
//     *
//     * @return the auth token.
//     */
//    public AuthToken getAuthToken() {
//        return authToken;
//    }
//
//    /**
//     * Sets the auth token.
//     *
//     * @param authToken the auth token.
//     */
//    public void setAuthToken(AuthToken authToken) {
//        this.authToken = authToken;
//    }
//
//    /**
//     * Returns the follower whose followees are to be counted by this request.
//     *
//     * @return the follower.
//     */
//    public String getFollowerAlias() {
//        return followerAlias;
//    }
//
//    /**
//     * Sets the follower.
//     *
//     * @param followerAlias the follower.
//     */
//    public void setFollowerAlias(String followerAlias) {
//        this.followerAlias = followerAlias;
//    }
//
//    @Override
//    public String toString() {
//        return "GetFollowingCountRequest{" +
//                "authToken=" + authToken.toString() +
//                ", followerAlias='" + followerAlias + '\'' +
//                '}';
//    }
//}
