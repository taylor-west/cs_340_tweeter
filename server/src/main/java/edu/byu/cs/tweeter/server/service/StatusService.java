package edu.byu.cs.tweeter.server.service;
// RECEIVES CALLS FROM THE LAMBDA (SERVER.HANDLERS) AND RETURNS FAKE DATA

import static edu.byu.cs.tweeter.server.sqs.StatusSqsClient.POSTS_Q_URL;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import edu.byu.cs.tweeter.server.models.DataPage;
import edu.byu.cs.tweeter.server.sqs.StatusSqsClient;

public class StatusService extends Service {

    public StatusService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        checkAuthToken(request.getAuthToken());
        checkAlias(request.getTargetUserAlias(), "target user");

        DataPage<Status> statusPage = daoFactory.getStatusDAO().getStory(request.getTargetUser(), request.getLimit(), request.getLastItem());
//        Pair<List<Status>, Boolean> pair = daoFactory.getStatusDAO().getStory(request.getTargetUserAlias(), request.getLimit(), request.getLastItem());
        return new GetStoryResponse(statusPage.getValues(), statusPage.getHasMorePages());
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        checkAuthToken(request.getAuthToken());
        checkLimit(request.getLimit());

        DataPage<Status> feedPage = daoFactory.getStatusDAO().getFeed(request.getTargetUserAlias(), request.getLimit(), request.getLastItem());
        return new GetFeedResponse(feedPage.getValues(), feedPage.getHasMorePages());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        checkAuthToken(request.getAuthToken());
        checkStatus(request.getStatus());

        // passes the request to the Follow Fetcher Queue, then returns true
        // literally lies to the user (as per the specs)
        StatusSqsClient.addMessageToQueue(request, POSTS_Q_URL);

        return new PostStatusResponse();
    }


    private void checkStatus(Status status){
        if(status == null){
            throw new RuntimeException("[Bad Request] Missing the status");
        }
    }
}
