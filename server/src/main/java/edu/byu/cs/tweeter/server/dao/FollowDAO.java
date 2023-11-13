package edu.byu.cs.tweeter.server.dao;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO {

    /**
     * Adds a new association between the given follower and followee. The current implementation uses
     * generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the follower (User who is following the followee)
     * @param followeeAlias the alias of the followee (User who is being followed by the follower)
     */
    public void follow(String followerAlias, String followeeAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followerAlias != null;
        assert followeeAlias != null;

        User follower = getFakeData().findUserByAlias(followerAlias);
        User followee = getFakeData().findUserByAlias(followeeAlias);
        return;
    }

    /**
     * Removes an existing  association between the given follower and followee. The current
     * implementation uses generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the follower (User who was following the followee)
     * @param followeeAlias the alias of the followee (User who was being followed by the follower)
     */
    public void unfollow(String followerAlias, String followeeAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followerAlias != null;
        assert followeeAlias != null;

        User follower = getFakeData().findUserByAlias(followerAlias);
        User followee = getFakeData().findUserByAlias(followeeAlias);

        return;
    }

    /**
     * Checks the database to see if the follower is currently following the followee. The current \
     * implementation uses dummy data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the follower (User who should be following the followee)
     * @param followeeAlias the alias of the followee (User who should be followed by the follower)
     *
     * @return a boolean indicating whether or not the follower is currently following the followee
     */
    public boolean isFollower(String followerAlias, String followeeAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followerAlias != null;
        assert followeeAlias != null;

        Boolean isFollower = getDummyIsFollower(followerAlias, followeeAlias);
        return isFollower;
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the User whose count of how many following is desired.
     * @return said count.
     */
    public Integer getFolloweeCount(String followerAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followerAlias != null;
        return getDummyFollowees().size();
    }

    /**
     * Gets the count of users from the database that are following (are followers of) the user
     * specified. The current implementation uses generated data and doesn't actually access a
     * database.
     *
     * @param followeeAlias the alias of the User whose count of how many followers is desired.
     * @return said count.
     */
    public Integer getFollowerCount(String followeeAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followeeAlias != null;
        return getDummyFollowers().size();
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the user whose followees are to be returned
     * @param limit the number of followees to be returned in one page
     * @param lastFolloweeAlias the alias of the last followee in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followees.
     */
    public Pair<List<User>, Boolean> getFollowing(String followerAlias, int limit, String lastFolloweeAlias) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert followerAlias != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(limit);
        System.out.println("FollowDAO found " + allFollowees.size() + " total followees for user: " + followerAlias); // TODO: remove

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFollowingStartingIndex(lastFolloweeAlias, allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new Pair<>(responseFollowees, hasMorePages);
    }

    /**
     * Gets the users from the database that are following the user specified in the request. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followeeAlias the alias of the user whose followers are to be returned
     * @param limit the number of followers to be returned in one page
     * @param lastFollowerAlias the alias of the last follower in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followers.
     */
    public Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert followeeAlias != null;

        List<User> allFollowers = getDummyFollowers();
        List<User> responseFollowers = new ArrayList<>(limit);
        System.out.println("FollowDAO found " + allFollowers.size() + " total followers for user: " + followeeAlias); // TODO: remove

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allFollowers != null) {
                int followeesIndex = getFollowersStartingIndex(lastFollowerAlias, allFollowers);

                for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowers.size();
            }
        }

        return new Pair<>(responseFollowers, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFollowingStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Determines the index for the first follower in the specified 'allFollowers' list that should
     * be returned in the current request. This will be the index of the next follower after the
     * specified 'lastFollower'.
     *
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowers the generated list of followers from which we are returning paged results.
     * @return the index of the first follower to be returned.
     */
    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }

        return followersIndex;
    }

    /**
     * Returns a boolean of whether or not the follower is following the followee. This is written
     * as a separate method to allow mocking.
     *
     * @return isFollower.
     */
    Boolean getDummyIsFollower(String followerAlias, String followeeAlias) {
        boolean isFollower = new Random().nextInt() > 0;
        return isFollower;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the list of dummy follower data. This is written as a separate method to allow
     * mocking of the followers.
     *
     * @return the followers.
     */
    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
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
