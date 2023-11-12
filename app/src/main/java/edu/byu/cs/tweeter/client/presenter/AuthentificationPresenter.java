package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observers.AuthenticationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

abstract public class AuthentificationPresenter extends BasePresenter implements AuthenticationObserver {
    public interface AuthentificationView extends View {
        void navigateToUser(User user);
    }

    protected UserService userService;

    public AuthentificationPresenter(AuthentificationView view) {
        // An assertion would be better, but Android doesn't support Java assertions
        if(view == null) {
            throw new NullPointerException();
        }
        this.view = view;
    }

    protected boolean validate(boolean isRegister, String alias, String password, String firstName,
                               String lastName, byte[] imageToUpload) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            getView().showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() == 0) {
            getView().showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.length() < 2) {
            getView().showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            getView().showErrorMessage("Password cannot be empty.");
            return false;
        }
        if (isRegister) {
            if (firstName.length() == 0) {
                getView().showErrorMessage("First Name cannot be empty.");
                return false;
            }
            if (lastName.length() == 0) {
                getView().showErrorMessage("Last Name cannot be empty.");
                return false;
            }

            if (imageToUpload == null || imageToUpload.length <= 0) {
                getView().showErrorMessage("Profile image must be uploaded.");
                return false;
            }
        }

        return true;
    }

    @Override
    public void authenticationSucceeded(AuthToken authToken, User user) {
        // Cache user session information
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        getView().showInfoMessage("Hello, " + user.getName());
        getView().navigateToUser(user);
    }

    //// BasePresenter methods ////
    protected AuthentificationView getView() {
        return (AuthentificationView) view;
    }

    @Override
    public void handleFailure(String message) {
        getView().showErrorMessage(message);
    }

    @Override
    public void handleExceptions(String message) {
        getView().showErrorMessage(message);
    }
    //// BasePresenter methods ////

}
