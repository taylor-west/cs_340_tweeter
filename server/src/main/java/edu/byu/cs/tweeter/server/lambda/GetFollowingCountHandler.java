package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingCountHandler implements RequestHandler<GetFollowingCountRequest, GetFollowingCountResponse> {

    /**
     * Returns the number of followees of the given follower (users that are being followed by
     * the user given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the number of followees.
     */
    @Override
    public GetFollowingCountResponse handleRequest(GetFollowingCountRequest request, Context context) {
        System.out.println("GetFollowingCountHandler has received a request: " + request.toString());
        FollowService service = new FollowService(new DynamoDAOFactory());
        GetFollowingCountResponse response = service.getFollowingCount(request);
        System.out.println("GetFollowingCountHandler.handleRequest returning response: " + response.toString());
        return response;
    }
}
