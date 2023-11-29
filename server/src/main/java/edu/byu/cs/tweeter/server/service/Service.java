package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;

public abstract class Service {
    protected DAOFactory daoFactory;

    Service(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    protected void checkAuthToken(AuthToken authToken, String userAlias){
        if(authToken == null) {
            throw new RuntimeException("[AuthError] Missing AuthToken");
        }else if(userAlias == null) {
            throw new RuntimeException("[AuthError] Missing user alias with which to validate AuthToken");
        }

        boolean authTokenIsValid = daoFactory.getAuthTokenDAO().verifyAuthTokenIsValid(authToken);
        if(authTokenIsValid){
            daoFactory.getAuthTokenDAO().refreshAuthToken(authToken);
        }else{
            daoFactory.getAuthTokenDAO().deleteAuthToken(authToken);
            throw new RuntimeException("[AuthError] Invalid AuthToken");
        }
    }

    protected void checkAlias(String alias, String aliasType){
        if(alias == null) {
            throw new RuntimeException("[Bad Request] Request is missing a " + aliasType + " alias");
        }
    }

    /**
     * Verifies that a user with the given alias exists in the database
     * @param alias
     * @param aliasType
     */
    protected void checkUser(String alias, String aliasType){
        if(alias == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a " + aliasType + " alias");
        }

        // check that the alias belongs to an existing user
        User user = daoFactory.getUserDAO().getUser(alias);
        if(user == null){
            throw new RuntimeException("[Bad Request] Alias " + alias + " does not exist");
        }
    }

    protected void checkLimit(int limit){
        if(limit <= 0) {
            throw new RuntimeException("[Bad Request] Limit must be a positive number");
        }
    }
}
