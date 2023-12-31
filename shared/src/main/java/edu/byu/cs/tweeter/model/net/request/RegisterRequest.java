package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest extends UnauthenticatedRequest{
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
    private String imageByteString;

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
     * @param imageByteString The user's profile picture (encoded as a string)
     */
    public RegisterRequest(String username, String password, String firstName, String lastName, String imageByteString) {
        super(username, password, DEFAULT_AUTHTOKEN_LIFESPAN_IN_SECONDS);

        this.firstName = firstName;
        this.lastName = lastName;
        this.imageByteString = imageByteString;
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
    public String getImageByteString() {
        return imageByteString;
    }

    /**
     * Sets the user's profile image.
     *
     * @param imageByteString the user's profile image.
     */
    public void setImageByteString(String imageByteString) {
        this.imageByteString = imageByteString;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", imageByteString='" + imageByteString + '\'' +
                '}';
    }
}
