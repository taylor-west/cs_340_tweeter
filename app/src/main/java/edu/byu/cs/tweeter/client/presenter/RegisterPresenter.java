package edu.byu.cs.tweeter.client.presenter;

import java.util.Base64;

import edu.byu.cs.tweeter.client.presenter.observers.AuthenticationObserver;


public class RegisterPresenter extends AuthentificationPresenter implements AuthenticationObserver {
    public RegisterPresenter(AuthentificationView view) {
        super(view);
    }

    protected AuthentificationView getView() {
        return (AuthentificationView) view;
    }

    public void register(String firstName, String lastName, String alias,
                         String password, byte[] imageBytes) {


        if (validate(true, alias, password, firstName, lastName, imageBytes)) {
            view.showInfoMessage("Registering...");

            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // Send register request.
            this.userService = getUserService();
            userService.register(firstName, lastName, alias, password,
                    imageBytesBase64, this);
        }
    }
}
