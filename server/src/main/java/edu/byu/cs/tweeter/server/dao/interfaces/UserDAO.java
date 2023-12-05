package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {
    /**
     * Gets the user from the database with the alias specified. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param userAlias the alias/username of the User whose information we are trying to find
     * @return the user with the given alias.
     */
    User getUser(String userAlias);

    /**
     * Compares the given username and password against the database to verify if the login is
     * valid. The current implementation uses generated data and doesn't actually access a database.
     *
     * @param username the alias/username of the User whose is attempting to login
     * @param rawPassword the plain password of the User whose is attempting to login
     * @return the user matching the credentials provided
     */
    User login(String username, String rawPassword);

    /**
     * Creates a new User entry in the database if the login is valid. The current implementation
     * uses generated data and doesn't actually access a database.
     * Returns the newly created User and an AuthToken
     *
     * @param username the desired alias/username of the User who is attempting to register
     * @param password the password of the User who is attempting to register
     * @param firstName the first name of the User who is attempting to register
     * @param lastName the last name of the User who is attempting to register
     * @param imageByteString a string representing the Base64 encoded profile image for the User
     *                        who is attempting to register
     * @return a new user with the specified details
     */
    User register(String username, String password, String firstName, String lastName, String imageByteString);


    /**
     * Returns the number of users that the given user is following (how many users are followees of the current user).
     * @param userAlias the alias of the user who is the follower (for who we will count followees)
     * @return number of users that are being followed by the given user (number of followees)
     */
    int getFollowingCount(String userAlias);

    /**
     * Returns the number of users that are following the given user (how many users are followers of the current user).
     * @param userAlias the alias of the user who is the followee (for who we will count followers)
     * @return number of users that are following the given user (number of followers)
     */
    int getFollowersCount(String userAlias);


    /**
     * Increases or decreases the "followersCount" value of a user by the amount given.
     * @param userAlias the user whose "followersCount" value will be changed
     * @param amount the amount the the "followersCount" should be changed (positive or negative).
     */
    void changeFollowersCount(String userAlias, Integer amount);

    /**
     * Increases or decreases the "followingCount" value of a user by the amount given.
     * @param userAlias the user whose "followingCount" value will be changed
     * @param amount the amount the the "followingCount" should be changed (positive or negative).
     */
    void changeFollowingCount(String userAlias, Integer amount);

}
