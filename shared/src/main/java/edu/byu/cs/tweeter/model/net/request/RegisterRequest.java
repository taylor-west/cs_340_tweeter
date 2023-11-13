package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterRequest {
    /**
     * The user's alias/username/handle
     */
    private String username;

    /**
     * The user's password
     */
    private String password;

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private RegisterRequest() {}

    /**
     * Creates an instance
     * @param username The user's alias/username/handle
     * @param password The user's password
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param image The user's profile picture (encoded as a string)
     */
    public RegisterRequest(String username, String password, String firstName, String lastName, String image) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the firstName (user's first name).
     *
     * @return the user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName (user's first name).
     *
     * @param firstName the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the lastName (user's last name).
     *
     * @return the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName (user's last name).
     *
     * @param lastName the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's profile image.
     *
     * @return the user's profile image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the user's profile image.
     *
     * @param image the user's profile image.
     */
    public void setImage(String image) {
        this.image = image;
    }
}
