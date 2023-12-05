package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the followers/users that are following the specified user
 */
public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {

    /**
     * Returns the users that are following the user specified in the request. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers.
     */
    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        System.out.println("GetFollowersHandler has received a request: " + request.toString());
        FollowService service = new FollowService(new DynamoDAOFactory());
        FollowersResponse response = service.getFollowers(request);
        System.out.println("GetFollowersHandler.handleRequest returning response with followers: " + response.getFollowers());
        return response;
    }
}
