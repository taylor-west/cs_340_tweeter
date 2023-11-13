package edu.byu.cs.tweeter.server.dao;
// RECEIVES CALLS FROM THE SERVER.SERVICE CLASSES AND RETURNS FAKE DATA
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class UserDAO {

    /**
     * Gets the user from the database with the alias specified. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param userAlias the alias/username of the User whose information we are trying to find
     * @return the user with the given alias.
     */
    public User getUser(String userAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert userAlias != null;
        User targetUser = getFakeData().findUserByAlias(userAlias);
        return targetUser;
    }

    /**
     * Compares the given username and password against the database to verify if the login is
     * valid. The current implementation uses generated data and doesn't actually access a database.
     *
     * @param username the alias/username of the User whose is attempting to login
     * @param password the password of the User whose is attempting to login
     * @return the user with the given alias.
     */
    public Pair<User, AuthToken> login(String username, String password) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert username != null;
        assert password != null;

        if(true){
            // TODO: actually check the password
        }

        User targetUser = getFakeData().findUserByAlias(username);
        AuthToken userAuthToken = getDummyAuthToken();
        return new Pair<User, AuthToken>(targetUser, userAuthToken);
    }

    /**
     * Removes the given authToken from the database. The current implementation uses generated
     * data and doesn't actually access a database.
     *
     * @param authToken the authToken of the User whose is attempting to logout
     */
    public void logout(AuthToken authToken) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert authToken != null;

        try{
            // TODO: actually remove the authToken
            doDummyRemoveAuthToken();
        }finally{
            return;
        }
    }

    /**
     * Creates a new User entry in the database if the login is valid. The current implementation
     * uses generated data and doesn't actually access a database.
     * Returns the newly created User and an AuthToken
     *
     * @param username the desired alias/username of the User whose is attempting to register
     * @param password the password of the User whose is attempting to register
     * @param firstName the first name of the User whose is attempting to register
     * @param lastName the last name of the User whose is attempting to register
     * @param image the desired profile image of the User whose is attempting to register, encoded
     *              as a string
     * @return a new user with the specified details and an AuthToken
     */
    public Pair<User, AuthToken> register(String username, String password, String firstName, String lastName, String image) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert username != null;
        assert password != null;
        assert firstName != null;
        assert lastName != null;
        assert image != null;

        if(true){
            // TODO: actually check the password
        }

        User newUser = getFakeData().getFirstUser();
        AuthToken userAuthToken = getDummyAuthToken();
        return new Pair<User, AuthToken>(newUser, userAuthToken);
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
     * Removes the given AuthToken from the database.
     * This is written as a separate method to allow for mocking.
     */
    void doDummyRemoveAuthToken() {
        return;
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
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
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
