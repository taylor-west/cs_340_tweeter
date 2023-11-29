package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that unfollows a user from another user
 */
public class UnfollowHandler implements RequestHandler<UnfollowRequest, UnfollowResponse> {
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest unfollowRequest, Context context) {
        FollowService followService = new FollowService(new DynamoDAOFactory());
        System.out.println("UnfollowHandler has received a request: " + unfollowRequest.toString());
        return followService.unfollow(unfollowRequest);
    }
}
