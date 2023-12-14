package edu.byu.cs.tweeter.server.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.JsonSerializer;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedsRequest;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;

public class JobSqsHandler implements RequestHandler<SQSEvent, Void> {
    private final DAOFactory daoFactory = new DynamoDAOFactory();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            UpdateFeedsRequest updateFeedsRequest = JsonSerializer.deserialize(msg.getBody(), UpdateFeedsRequest.class);
            System.out.println("---- JobSqsHandler received request: " + updateFeedsRequest.toString());
            assert updateFeedsRequest.getStatus() != null;
            assert updateFeedsRequest.getFollowerAliases() != null;

            Status status = updateFeedsRequest.getStatus();
            List<String> followerAliases = updateFeedsRequest.getFollowerAliases();

            // posts the status to the feeds of all of the followers
            StatusDAO statusDAO = daoFactory.getStatusDAO();

//            System.out.println("       attempting to run batchPostToFeed with followerAliases: " + followerAliases.toString() + ", status: " + status.toString());
            statusDAO.batchPostToFeed(followerAliases, status);

            System.out.println("---- exit JobSqsHandler");
        }
        return null;
    }

}