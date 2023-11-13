package edu.byu.cs.tweeter.server.dao;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'status' data from the database.
 */
public class StatusDAO {

    /**
     * Gets statuses from the feed of the user specified in the request. Uses information in the
     * request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param userAlias the alias of the user whose Story/statuses are to be returned
     * @param limit the number of statuses to be returned in one page
     * @param lastStatus the last status in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the Story/statuses of the given user.
     */
    public Pair<List<Status>, Boolean> getStory(String userAlias, int limit, Status lastStatus) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert userAlias != null;

        List<Status> allStatuses = getDummyStory();
        List<Status> responseStatuses = new ArrayList<>(limit);
        System.out.println("StatusDAO found " + allStatuses.size() + " total statuses in the Story of user: " + userAlias); // TODO: remove

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allStatuses != null) {
                int followeesIndex = getStartingIndex(lastStatus, allStatuses);

                for(int limitCounter = 0; followeesIndex < allStatuses.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseStatuses, hasMorePages);
    }

    /**
     * Gets statuses from the feed of the user specified in the request. Uses information in the
     * request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param userAlias the alias of the user whose feed is to be returned
     * @param limit the number of statuses to be returned in one page
     * @param lastStatus the last status in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the feed for the given user, comprised of statuses of the user's following/followees.
     */
    public Pair<List<Status>, Boolean> getFeed(String userAlias, int limit, Status lastStatus) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert userAlias != null;

        List<Status> allStatuses = getDummyFeed();
        List<Status> responseStatuses = new ArrayList<>(limit);
        System.out.println("StatusDAO found " + allStatuses.size() + " total statuses in the Feed of user: " + userAlias); // TODO: remove

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allStatuses != null) {
                int statusIndex = getStartingIndex(lastStatus, allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < limit; statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseStatuses, hasMorePages);
    }

    /**
     * Inserts a new status into the database which is associated with the user specified in the
     * request.The current implementation returns generated data and doesn't actually access a
     * database.
     *
     * @param userAlias the alias of the user who is posting the status
     * @param status the status to post
     */
    public void postStatus(String userAlias, Status status) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert userAlias != null;
        assert status != null;

        try{
            postDummyStatus(userAlias, status);
        }finally{}

        return;
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next status after the
     * specified 'lastStatus'.
     *
     * @param lastStatus the last status that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first status to be returned.
     */
    private int getStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusesIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    /**
     * Adds a new status to the database which is associated with the given user.. This is written
     * as a separate method to allow for mocking.
     *
     * @return the statuses.
     */
    void postDummyStatus(String userAlias, Status status) {
        return;
    }

    /**
     * Returns a dummy Feed via a list of dummy status data. This is written as a separate method
     * to allow mocking of the statuses.
     *
     * @return the statuses.
     */
    List<Status> getDummyFeed() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns a dummy Story via a list of dummy status data. This is written as a separate method
     * to allow mocking of the statuses.
     *
     * @return the statuses.
     */
    List<Status> getDummyStory() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
