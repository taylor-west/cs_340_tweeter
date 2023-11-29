package edu.byu.cs.tweeter.server.service;
// RECEIVES CALLS FROM THE LAMBDA (SERVER.HANDLERS) AND RETURNS FAKE DATA

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService extends Service {

    public UserService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Error in UserService.getUser: target user alias '" + request.getTargetUserAlias() + "' does not exist");
        }

        User user = daoFactory.getUserDAO().getUser(request.getTargetUserAlias());

        return new GetUserResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        checkAlias(request.getUsername(), "user");
        checkPassword(request.getUsername(), request.getPassword());

        User loggedInUser = daoFactory.getUserDAO().login(request.getUsername(), request.getPassword());
        if (loggedInUser != null) {
            AuthToken loggedInAuthToken = daoFactory.getAuthTokenDAO().addAuthToken(request.getDesiredAuthTokenLifespan());
            return new LoginResponse(loggedInUser, loggedInAuthToken);
        }else{
            return new LoginResponse(null, null);

        }
    }

    public LogoutResponse logout(LogoutRequest request) {
        checkAuthToken(request.getAuthToken(), request.getCurrUserAlias());

        // remove the user's AuthToken from the database
        daoFactory.getAuthTokenDAO().deleteAuthToken(request.getAuthToken());

        return new LogoutResponse();
    }

    public RegisterResponse register(RegisterRequest request) {
        checkPassword(request.getUsername(), request.getPassword());

        if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing a profile image");
        }



        User registeredUser = daoFactory.getUserDAO().register(request.getUsername(),
                request.getPassword(), request.getFirstName(), request.getLastName(),
                request.getImage());
        AuthToken loggedInAuthToken = daoFactory.getAuthTokenDAO().addAuthToken(request.getDesiredAuthTokenLifespan());
        return new RegisterResponse(registeredUser, loggedInAuthToken);
    }

    private void checkPassword(String userAlias, String password) {
        if (userAlias == null) {
            throw new RuntimeException("[Bad Request] Missing user alias");
        } else if (password == null) {
            throw new RuntimeException("[Bad Request] Missing password");
        }
    }

}