package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver{
    public RegisterPresenter(View view) {
        this.view = view;
    }

    @Override
    public void registerSucceeded(AuthToken authToken, User user) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello, " + user.getName());
        view.openMainView(user);
    }

    @Override
    public void registerFailed(String message) {
        view.showErrorMessage(message);
    }

    // every Presenter class should define the interface for the view
    public interface View {
        void showInfoMessage(String message);
        void showErrorMessage(String message);

        void hideErrorMessage();

        void hideInfoMessage();

        void openMainView(User user);

    }

    private View view;

    public void register(String firstName, String lastName, String alias,
                         String password, Drawable imageToUpload){

        if( validateRegistration(firstName, lastName, alias, password, imageToUpload) ){
            view.hideErrorMessage();
            view.showInfoMessage("Registering...");

            // Convert image to byte array.
            String imageBytesBase64 = convertDrawableToBytesBase64(imageToUpload);

            // Send register request.
            var userService = new UserService();
            userService.register(firstName, lastName, alias, password,
                    imageBytesBase64, this);
        }
    }

    private boolean validateRegistration(String firstName, String lastName, String alias,
                                     String password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            view.showErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.showErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (alias.length() == 0) {
            view.showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }
        if (imageToUpload == null) {
            view.showErrorMessage("Profile image must be uploaded.");
            return false;
        }

        return true;
    }

    private String convertDrawableToBytesBase64(Drawable image){
        Bitmap bitmapImage = ((BitmapDrawable) image).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
