package edu.byu.cs.tweeter.server.service;
// RECEIVES CALLS FROM THE LAMBDA (SERVER.HANDLERS) AND RETURNS FAKE DATA
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    public GetStoryResponse getStory(GetStoryRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a alias/username");
        }

        Pair<List<Status>, Boolean> pair = getStatusDAO().getStory(request.getUserAlias(), request.getLimit(), request.getLastStatus());
        return new GetStoryResponse(pair.getFirst(), pair.getSecond());
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a alias/username");
        }else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing an authToken");
        }else if(request.getLimit() <= 0){
            throw new RuntimeException("[Bad Request] Limit must be a positive value");
        }

        Pair<List<Status>, Boolean> pair = getStatusDAO().getFeed(request.getUserAlias(), request.getLimit(), request.getLastStatus());
        return new GetFeedResponse(pair.getFirst(), pair.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a alias/username");
        } else if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Missing the status");
        }


        getStatusDAO().postStatus(request.getUserAlias(), request.getStatus());
        return new PostStatusResponse();
    }

    /**
     * Returns an instance of {@link StatusDAO}. Allows mocking of the StatusDAO class
     * for testing purposes. All usages of StatusDAO should get their StatusDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StatusDAO getStatusDAO() {
        return new StatusDAO();
    }
}
