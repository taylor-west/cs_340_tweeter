package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class GetStoryHandler implements RequestHandler<GetStoryRequest, GetStoryResponse> {
    @Override
    public GetStoryResponse handleRequest(GetStoryRequest getStoryRequest, Context context) {
        System.out.println("GetStoryHandler has received a request: " + getStoryRequest.toString());
        StatusService statusService = new StatusService(new DynamoDAOFactory());
        GetStoryResponse response = statusService.getStory(getStoryRequest);
        System.out.println("GetStoryHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }
}