package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observers.AuthenticationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter extends AuthentificationPresenter implements AuthenticationObserver {
    private static final String LOG_TAG = "LoginPresenter";

    public LoginPresenter(AuthentificationView view) {
        super(view);
    }

    public void login(String alias, String password) {
        if (validate(false, alias, password, null, null, null)) {
            view.showInfoMessage("Logging in...");

            // Send login request.
            this.userService = getUserService();
            userService.login(alias, password, this);


        }
    }


    //// BasePresenter methods ////
    protected AuthentificationView getView() {
        return (AuthentificationView) view;
    }
    ////   ////
}
