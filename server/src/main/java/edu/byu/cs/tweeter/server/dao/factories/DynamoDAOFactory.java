package edu.byu.cs.tweeter.server.dao.factories;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDAO;

public class DynamoDAOFactory implements DAOFactory {
    private DynamoUserDAO userDAO;
    private DynamoFollowDAO followDAO;
    private DynamoStatusDAO statusDAO;
    private DynamoAuthTokenDAO authTokenDAO;


    /**
     * Returns an instance of {@link DynamoUserDAO}. Allows mocking of the UserDAO class
     * for testing purposes. All usages of UserDAO should get their UserDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    @Override
    public DynamoUserDAO getUserDAO() {
        System.out.println("in DynamoDAOFactory.getUserDAO");
        if(userDAO == null){
            System.out.println("  getUserDAO: creating new instance of DynamoUserDAO...");
            this.userDAO = new DynamoUserDAO();
            System.out.println("  getUserDAO: finished creating new instance of DynamoUserDAO");
        }
            return userDAO;
    }

    /**
     * Returns an instance of {@link DynamoFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    @Override
    public DynamoFollowDAO getFollowDAO() {
        if(followDAO == null){
            this.followDAO = new DynamoFollowDAO();
        }

        return followDAO;
    }

    /**
     * Returns an instance of {@link DynamoStatusDAO}. Allows mocking of the StatusDAO class
     * for testing purposes. All usages of StatusDAO should get their StatusDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    @Override
    public DynamoStatusDAO getStatusDAO() {
        if(statusDAO == null){
            this.statusDAO = new DynamoStatusDAO();
        }

        return statusDAO;
    }

    /**
     * Returns an instance of {@link DynamoAuthTokenDAO}. Allows mocking of the AuthTokenDAO class
     * for testing purposes. All usages of AuthTokenDAO should get their AuthTokenDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    @Override
    public DynamoAuthTokenDAO getAuthTokenDAO() {
        if(authTokenDAO == null){
            this.authTokenDAO = new DynamoAuthTokenDAO();
        }

        return authTokenDAO;
    }
}
