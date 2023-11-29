package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user out.
 */
public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest logoutRequest, Context context) {
        UserService userService = new UserService(new DynamoDAOFactory());
        System.out.println("LogoutHandler has received a request: " + logoutRequest.toString());
        return userService.logout(logoutRequest);
    }
}
