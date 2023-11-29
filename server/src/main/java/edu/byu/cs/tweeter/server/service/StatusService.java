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
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import edu.byu.cs.tweeter.server.models.DataPage;

public class StatusService extends Service {

    public StatusService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        checkAlias(request.getTargetUserAlias(), "target user");

        DataPage<Status> statusPage = daoFactory.getStatusDAO().getStory(request.getTargetUser(), request.getLimit(), request.getLastItem());
//        Pair<List<Status>, Boolean> pair = daoFactory.getStatusDAO().getStory(request.getTargetUserAlias(), request.getLimit(), request.getLastItem());
        return new GetStoryResponse(statusPage.getValues(), statusPage.getHasMorePages());
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        checkLimit(request.getLimit());

        DataPage<Status> feedPage = daoFactory.getStatusDAO().getFeed(request.getTargetUserAlias(), request.getLimit(), request.getLastItem());
        return new GetFeedResponse(feedPage.getValues(), feedPage.getHasMorePages());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        checkStatus(request.getStatus());

        // pull a list of aliases for all Users who follow the posting (current) user
        List<String> followersAliases = daoFactory.getFollowDAO().getFollowersAliases(request.getCurrUserAlias());

        // add the Status to the feed of each follower
        for(String followerAlias : followersAliases){
            daoFactory.getStatusDAO().postToFeed(followerAlias, request.getStatus());
        }

        // add the Status to the story of the posting (current) user
        daoFactory.getStatusDAO().postStatus(request.getStatus());

        return new PostStatusResponse();
    }

    private void checkStatus(Status status){
        if(status == null){
            throw new RuntimeException("[Bad Request] Missing the status");
        }
    }
}
