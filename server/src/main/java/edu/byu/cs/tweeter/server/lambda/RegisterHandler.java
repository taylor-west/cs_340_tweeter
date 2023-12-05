package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that registers a new user and returns the user object and an auth code for
 * a successful register/login.
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest registerRequest, Context context) {
        UserService userService = new UserService(new DynamoDAOFactory());
        System.out.println("RegisterHandler has received a request: " + registerRequest.toString());
        return userService.register(registerRequest);
    }
}
