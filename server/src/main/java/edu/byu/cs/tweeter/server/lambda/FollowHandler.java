package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class FollowHandler implements RequestHandler<FollowRequest, FollowResponse> {
    @Override
    public FollowResponse handleRequest(FollowRequest followRequest, Context context) {
        FollowService followService = new FollowService(new DynamoDAOFactory());
        System.out.println("LoginHandler has received a request: " + followRequest.toString());
        return followService.follow(followRequest);
    }
}
