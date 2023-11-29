package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that posts a status to a user returns  the user object and an auth code for
 * a successful login.
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest postStatusRequest, Context context) {
        StatusService statusService = new StatusService(new DynamoDAOFactory());
        System.out.println("PostStatusHandler has received a request: " + postStatusRequest.toString());
        return statusService.postStatus(postStatusRequest);
    }
}
