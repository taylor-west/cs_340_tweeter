package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that returns the feed for a user specified in the request. The feed is
 *  comprised of the statuses of the followees of the given user.
 */
public class GetFeedHandler implements RequestHandler<GetFeedRequest, GetFeedResponse> {
    @Override
    public GetFeedResponse handleRequest(GetFeedRequest getFeedRequest, Context context) {
        System.out.println("GetFeedHandler has received a request: " + getFeedRequest.toString());
        StatusService statusService = new StatusService();
        GetFeedResponse response = statusService.getFeed(getFeedRequest);
        System.out.println("GetFeedHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }
}