package edu.byu.cs.tweeter.server.service;
// RECEIVES REQUESTS FROM THE LAMBDA (SERVER.HANDLERS) AND ROUTES THEM TO THE DAO
import java.util.List;

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
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    /**
     * Passes information about the follower and followee to the {@link FollowDAO} to determine
     * whether the first user (follower) is currently following the second user (followee)
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the first user (follower) is currently following the second user (followee)
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        Boolean isFollower = getFollowDAO().isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
        System.out.println("FollowService.isFollower is returning: " + isFollower);
        return new IsFollowerResponse(isFollower);
    }

    /**
     * Passes information about the follower and followee to the {@link FollowDAO} to create a new
     * 'follows' record that associates the two users.
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the follow was successful.
     */
    public FollowResponse follow(FollowRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        getFollowDAO().follow(request.getFollowerAlias(), request.getFolloweeAlias());
        System.out.println("FollowService.follow is returning");
        return new FollowResponse();
    }

    /**
     * Passes information about the follower and followee to the {@link FollowDAO} to remove an
     * existing 'follows' record that associated the two users.
     *
     * @param request contains the data required to fulfill the request.
     * @return whether the unfollow was successful.
     */
    public UnfollowResponse unfollow(UnfollowRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        getFollowDAO().unfollow(request.getFollowerAlias(), request.getFolloweeAlias());
        System.out.println("FollowService.unfollow is returning");
        return new UnfollowResponse();
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowing(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = getFollowDAO().getFollowing(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        System.out.println("FollowService.getFollowing is returning " + pair.getFirst().size() + " users");
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    /**
     * Returns the number of followees of the given follower (users that are being followed by
     * the user given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of followees.
     */
    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int followeeCount = getFollowDAO().getFolloweeCount(request.getFollowerAlias());
        System.out.println("FollowService.getFollowingCount is returning:" + followeeCount);
        return new GetFollowingCountResponse(followeeCount);
    }

    /**
     * Returns the number of followers of the given followee (users that are following the user
     * given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of followers.
     */
    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int followerCount = getFollowDAO().getFollowerCount(request.getFolloweeAlias());
        System.out.println("FollowService.getFollowersCount is returning:" + followerCount);
        return new GetFollowersCountResponse(followerCount);
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
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = getFollowDAO().getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        System.out.println("FollowService.getFollowers is returning " + pair.getFirst().size() + " users");
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
