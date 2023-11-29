package edu.byu.cs.tweeter.server.service;
// RECEIVES REQUESTS FROM THE LAMBDA (SERVER.HANDLERS) AND ROUTES THEM TO THE DAO

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.server.models.DataPage;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends Service {

    public FollowService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * Passes information about the follower and followee to the {@link DynamoFollowDAO} to determine
     * whether the first user (follower) is currently following the second user (followee)
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the first user (follower) is currently following the second user (followee)
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        checkAuthToken(request.getAuthToken(), request.getFollowerAlias());
        checkAlias(request.getFollowerAlias(), "follower");
        checkUser(request.getFollowerAlias(), "follower");
        checkAlias(request.getFolloweeAlias(), "followee");
        checkUser(request.getFollowerAlias(), "followee");

        Boolean isFollower = daoFactory.getFollowDAO().isFollower(request.getFollowerAlias(), request.getFolloweeAlias());

        // System.out.println("FollowService.isFollower is returning: " + isFollower);
        return new IsFollowerResponse(isFollower);
    }

    /**
     * Passes information about the follower and followee to the {@link FollowDAO} to create a new
     * 'follows' record that associates the two users. Also increments the "followersCount" and
     * "followingCount" values of the appropriate users via the {@link UserDAO}
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the follow was successful.
     */
    public FollowResponse follow(FollowRequest request) {
        checkAuthToken(request.getAuthToken(), request.getFollowerAlias());
        checkAlias(request.getFollowerAlias(), "follower");
        checkUser(request.getFollowerAlias(), "follower");
        checkAlias(request.getFolloweeAlias(), "followee");
        checkUser(request.getFollowerAlias(), "followee");

        // create a new Follow record
        daoFactory.getFollowDAO().follow(request.getFollower(), request.getFollowee());

        // update the "followersCount" and "followingCount" values for the appropriate users
        daoFactory.getUserDAO().changeFollowersCount(request.getFolloweeAlias(), 1);
        daoFactory.getUserDAO().changeFollowingCount(request.getFollowerAlias(), 1);

        // System.out.println("FollowService.follow is returning");
        return new FollowResponse();
    }

    /**
     * Passes information about the follower and followee to the {@link DynamoFollowDAO} to remove
     * an existing 'follows' record that associated the two users. Also decrements the
     * "followersCount" and "followingCount" values of the appropriate users via the {@link UserDAO}
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the unfollow was successful.
     */
    public UnfollowResponse unfollow(UnfollowRequest request) {
        checkAuthToken(request.getAuthToken(), request.getFollowerAlias());
        checkAlias(request.getFollowerAlias(), "follower");
        checkUser(request.getFollowerAlias(), "follower");
        checkAlias(request.getFolloweeAlias(), "followee");
        checkUser(request.getFollowerAlias(), "followee");

        // remove an existing Follow record
        daoFactory.getFollowDAO().unfollow(request.getFollowerAlias(), request.getFolloweeAlias());

        // update the "followersCount" and "followingCount" values for the appropriate users
        daoFactory.getUserDAO().changeFollowersCount(request.getFolloweeAlias(), -1);
        daoFactory.getUserDAO().changeFollowingCount(request.getFollowerAlias(), -1);

        //  System.out.println("FollowService.unfollow is returning");
        return new UnfollowResponse();
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowing(FollowingRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        checkAlias(request.getFollowerAlias(), "follower");
        checkUser(request.getFollowerAlias(), "follower");
        checkLimit(request.getLimit());

        DataPage<User> followingUsersPage = daoFactory.getFollowDAO().getFollowing(request.getFollowerAlias(), request.getLimit(), request.getLastItem());

        // System.out.println("FollowService.getFollowing is returning " + pair.getFirst().size() + " users");
        return new FollowingResponse(followingUsersPage.getValues(), followingUsersPage.getHasMorePages());
    }

    /**
     * Returns the number of followees of the given follower (number of users that are followed by
     * the user specified in the request). Uses a {@link FollowDAO} to get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of followees.
     */
    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        checkAuthToken(request.getAuthToken(), request.getFollowerAlias());
        checkAlias(request.getFollowerAlias(), "follower");
        checkUser(request.getFollowerAlias(), "follower");

        int followeeCount = daoFactory.getUserDAO().getFollowingCount(request.getFollowerAlias());

        // System.out.println("FollowService.getFollowingCount is returning:" + followeeCount);
        return new GetFollowingCountResponse(followeeCount);
    }

    /**
     * Returns the users are following the user specified in the request. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public FollowersResponse getFollowers(FollowersRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        checkAlias(request.getFolloweeAlias(), "followee");
        checkUser(request.getFolloweeAlias(), "followee");
        checkLimit(request.getLimit());

        DataPage<User> followersUsersPage = daoFactory.getFollowDAO().getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastItem());

        // System.out.println("FollowService.getFollowers is returning " + pair.getFirst().size() + " users");
        return new FollowersResponse(followersUsersPage.getValues(), followersUsersPage.getHasMorePages());
    }

    /**
     * Returns the number of followers of the given followee (number of users that are following
     * the user specified in the request). Uses a {@link FollowDAO} to get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of followers.
     */
    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        checkAuthToken(request.getAuthToken(), request.getFolloweeAlias());
        checkAlias(request.getFolloweeAlias(), "followee");
        checkUser(request.getFolloweeAlias(), "followee");

        int followersCount = daoFactory.getUserDAO().getFollowersCount(request.getFolloweeAlias());

        // System.out.println("FollowService.getFollowersCount is returning:" + followerCount);
        return new GetFollowersCountResponse(followersCount);
    }
}
