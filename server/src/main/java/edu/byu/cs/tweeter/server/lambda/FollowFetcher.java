package edu.byu.cs.tweeter.server.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.JsonSerializer;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedsRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.models.DataPage;
import edu.byu.cs.tweeter.server.sqs.StatusSqsClient;

public class FollowFetcher implements RequestHandler<SQSEvent, Void> {
    private final DAOFactory daoFactory = new DynamoDAOFactory();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {


        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostStatusRequest postStatusRequest = JsonSerializer.deserialize(msg.getBody(), PostStatusRequest.class);
            System.out.println("---- FollowFetcher received request: " + postStatusRequest.toString());
            assert postStatusRequest.getStatus() != null;

            Status status = postStatusRequest.getStatus();
            User user = postStatusRequest.getStatus().getUser();
            AuthToken authToken = postStatusRequest.getAuthToken();

            // posts the status to the user's story
            StatusDAO statusDAO = daoFactory.getStatusDAO();
            statusDAO.postStatus(status);

            // posts the status to the feeds of all of the user's followers
            FollowDAO followDAO = daoFactory.getFollowDAO();

            boolean hasMorePages = true;
            String lastUserAlias = null;
            FollowersRequest followersRequest;
            FollowersResponse response;
            int loopCounter = 0;

            while (hasMorePages) {
//                System.out.println("  iteration in while statement: " + loopCounter);
                DataPage<String> userAliasPage = followDAO.getFollowersAliases(user.getAlias(), 200, lastUserAlias);

                int numUsersInPage = userAliasPage.getValues().size();
                if(numUsersInPage > 0){
//                    System.out.println("  this page has " + numUsersInPage + " followers on it. Adding requests to JobsQ");
                    lastUserAlias = userAliasPage.getValues().get(numUsersInPage - 1);

                    UpdateFeedsRequest updateFeedsRequest = new UpdateFeedsRequest(userAliasPage.getValues(), postStatusRequest.getStatus());

                    // passes a request to update feeds to the Jobs Queue SQS
                    StatusSqsClient.addMessageToQueue(updateFeedsRequest, StatusSqsClient.JOBS_Q_URL);
                }else{
                    System.out.println("  didn't find any followers for " + user.getAlias());
                    lastUserAlias = null;
                }

                hasMorePages = userAliasPage.getHasMorePages();
                loopCounter++;
            }
        }
        System.out.println("---- exit FollowFetcher");
        return null;
    }

}