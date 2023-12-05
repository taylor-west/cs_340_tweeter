package edu.byu.cs.tweeter.server.dao.factories;

import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public interface DAOFactory {
    UserDAO getUserDAO();
    FollowDAO getFollowDAO();
    StatusDAO getStatusDAO();
    AuthTokenDAO getAuthTokenDAO();
}
