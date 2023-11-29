package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAO {
    /**
     * Adds a new AuthToken to the database.
     * @return the AuthToken that was added to the database
     */
    AuthToken addAuthToken(int authTokenLifespanInSeconds);


    /**
     * Set the timestamp of the given AuthToken to the current time. This effectively "refreshes"
     * the window of validity for the AuthToken, extending its life.
     */
    void refreshAuthToken(AuthToken authToken);

    /**
     *  Checks that the given AuthToken exists in the database and is not expired,
     * @param authToken the AuthToken to validate
     * @return whether the AuthToken is present and un-expired
     */
    boolean verifyAuthTokenIsValid(AuthToken authToken);

    /**
     * Deletes an AuthToken from the database (usually because it is expired or the user logged out).
     */
    void deleteAuthToken(AuthToken authToken);
}
