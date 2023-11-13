package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://jtjzhd0d0d.execute-api.us-east-1.amazonaws.com/cs_340_tweeter";
    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);


    /**
     * Returns whether or not the request to mark the two users as follower/followee was successful.
     *
     * @param request contains information about the users who are to the follower/followee, and
     *                any other information required to satisfy the request.
     * @return whether or not the follower is now following the followee (if request was successful).
     */
    public FollowResponse follow(FollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        System.out.println("ServerFacade#follow with URL=" + urlPath);
        FollowResponse response = clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);
        return response;
    }

    /**
     * Returns whether or not the request to remove the association between the follower/followee
     * users was successful.
     *
     * @param request contains information about the users who are to the follower/followee, and
     *                any other information required to satisfy the request.
     * @return whether the follower is now unfollowed frp, the followee (if request was successful).
     */
    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        System.out.println("ServerFacade#unfollow with URL=" + urlPath);
        UnfollowResponse response = clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);
        return response;
    }


    /**
     * Returns whether the given follower (first user) currently follows the given followee
     * (second user).
     *
     * @param request contains information about the alleged follower/follwee users and any other
     *                information required to satisfy the request.
     * @return whether the follower current follows the followee
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
    }

    /**
     * Returns the User associated with the alias/username specified in the request.
     *
     * @param request contains information about the user who is to be returned and any other
     *               information required to satisfy the request.
     * @return the user.
     */
    public GetUserResponse getUser(GetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        System.out.println("ServerFacade#getUser with URL=" + urlPath);
        GetUserResponse response = clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
        return response;
    }

    /**
     * Returns the story/statuses of the user specified in the request. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains information about the user whose story/statuses are to be returned
     *                and any  other information required to satisfy the request.
     * @return the statuses.
     */
    public GetStoryResponse getStory(GetStoryRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        System.out.println("ServerFacade#getStory with URL=" + urlPath);
        GetStoryResponse response = clientCommunicator.doPost(urlPath, request, null, GetStoryResponse.class);
        return response;
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Performs a logout and if successful, returns whether the logout was successful
     *
     * @param request contains all information needed to perform a logout.
     * @return whether the logout was successful.
     */
    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }

    /**
     * Performs a register and if successful, returns the new (logged-in) user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the register response.
     */
    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowing(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
        return response;
    }

    /**
     * Returns the number of users that the user specified in the request is following (are
     * followees of the given use)
     *
     * @param request contains information about the user whose followees are to be counted, and
     *                any other information required to satisfy the request.
     * @return the number of followees.
     */
    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetFollowingCountResponse.class);
    }

    /**
     * Returns the users that are following (are followers of) the user specified in the request. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public FollowersResponse getFollowers(FollowersRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
    }

    /**
     * Returns the number of users that are following (are followers of) the user specified in the
     * request.
     *
     * @param request contains information about the user whose followers are to be counted, and
     *                any other information required to satisfy the request.
     * @return the number of followers.
     */
    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetFollowersCountResponse.class);
    }

    /**
     * Posts the given status for a user specified in the request.
     *
     * @param request contains information about the status that is to be posted and  the user that is to post it.
     * @return the status information about the status posted by the user.
     */
    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
    }

    /**
     * Gets the feed for a user specified in the request. The Feed is a list of statuses posted by
     * Users that are followees of the user specified in the request.
     *
     * @param getFeedRequest contains information about the user whose feed is to be retrieved
     * @return the user's feed
     */
    public GetFeedResponse getFeed(GetFeedRequest getFeedRequest, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, getFeedRequest, null, GetFeedResponse.class);
    }
}
