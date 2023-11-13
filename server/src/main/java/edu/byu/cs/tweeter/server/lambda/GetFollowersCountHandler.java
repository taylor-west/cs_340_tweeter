package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.logging.Handler;

import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersCountHandler implements RequestHandler<GetFollowersCountRequest, GetFollowersCountResponse> {
    /**
     * Returns the number of followers of the given followee (users that are following the user
     * given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the number of followers.
     */
    @Override
    public GetFollowersCountResponse handleRequest(GetFollowersCountRequest request, Context context) {
        System.out.println("GetFollowersCountHandler has received a request: " + request.toString());
        FollowService service = new FollowService();
        GetFollowersCountResponse response = service.getFollowersCount(request);
        System.out.println("GetFollowersCountHandler.handleRequest returning response: " + response.toString());
        return response;
    }
}
