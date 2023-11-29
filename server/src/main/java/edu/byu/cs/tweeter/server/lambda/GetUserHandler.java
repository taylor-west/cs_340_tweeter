package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that finds a user and returns information about that user
 */
public class GetUserHandler implements RequestHandler<GetUserRequest, GetUserResponse> {
    @Override
    public GetUserResponse handleRequest(GetUserRequest getUserRequest, Context context) {
        System.out.println("GetUserHandler has received a request: " + getUserRequest.toString());
        UserService userService = new UserService(new DynamoDAOFactory());
        GetUserResponse response = userService.getUser(getUserRequest);
        System.out.println("GetUserHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }
}