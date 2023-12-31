package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver{

    // every Presenter class should define the interface for the view
    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();
        void openMainView(User user);
    }

    private View view;

    public LoginPresenter(View view){
        this.view = view;
    }

    public void login(String alias, String password){
        if(validateLogin(alias, password)){
            view.hideErrorMessage();
            view.showInfoMessage("Logging in...");

            var userService = new UserService();
            userService.login(alias, password, this);
        }
    }

    public boolean validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage ("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }

        return true;
    }

    @Override
    public void loginSucceeded(AuthToken authToken, User user) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello, " + user.getName());
        view.openMainView(user);
    }

    @Override
    public void loginFailed(String message) {
        view.showErrorMessage(message);
    }
}
