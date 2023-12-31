package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest isFollowerRequest, Context context) {
        System.out.println("IsFollowerHandler has received a request: " + isFollowerRequest.toString());
        FollowService followService = new FollowService(new DynamoDAOFactory());
        IsFollowerResponse response = followService.isFollower(isFollowerRequest);
        System.out.println("IsFollowerHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }
}
