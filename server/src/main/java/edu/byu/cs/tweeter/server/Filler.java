package edu.byu.cs.tweeter.server;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterFollows;
import edu.byu.cs.tweeter.server.dao.dynamo.tables.TweeterUsers;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDAOFactory;
import edu.byu.cs.tweeter.util.Pair;

public class Filler {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "followed";
    private static final String FOLLOW_TARGET_ALIAS = "@test1";
    private static final String FOLLOW_TARGET_FIRST_NAME = "first1";
    private static final String FOLLOW_TARGET_LAST_NAME = "last1";
    private static final String FOLLOW_TARGET_IMAGE_URL = "https://cs340-wtaylorh-tweeter-images.s3.us-west-1.amazonaws.com/@test1";

    private static final DynamoDAOFactory dynamoDaoFactory = new DynamoDAOFactory();
    public static void fillDatabase() {
        System.out.println("\n\n\nFILL DATABASE");
        DynamoUserDAO userDAO = dynamoDaoFactory.getUserDAO();
        DynamoFollowDAO followDAO = dynamoDaoFactory.getFollowDAO();

        List<TweeterFollows> follows = new ArrayList<>();
        List<TweeterUsers> users = new ArrayList<>();

        // Iterate over the number of users you will create
//        for (int i = 7075; i < NUM_USERS; i++) {
//            TweeterUsers newUser = makeFakeUser(i);
//            users.add(newUser);
//        }

        for (int i = 8350; i < NUM_USERS; i++) {
            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            TweeterFollows newFollow = makeFakeFollow(i);
            follows.add(newFollow);
        }

        // Call the DAOs for the database logic
//        if (users.size() > 0) {
//            userDAO.batchCreateUser(users);
//            userDAO.changeFollowersCount(FOLLOW_TARGET_ALIAS, NUM_USERS);
//        }

        if (follows.size() > 0) {
            followDAO.batchFollow(follows);
        }
    }


    public static void emptyDatabases() {
        TweeterUsers followeeUser = new TweeterUsers();
        followeeUser.setAlias(FOLLOW_TARGET_ALIAS);
        followeeUser.setFirstName(FOLLOW_TARGET_FIRST_NAME);
        followeeUser.setLastName(FOLLOW_TARGET_LAST_NAME);
        followeeUser.setFollowingCount(1);
        followeeUser.setFollowersCount(19);
        followeeUser.setImageURL(FOLLOW_TARGET_IMAGE_URL);

        List<String> aliasList = new ArrayList<>();
        List<Pair<String, String>> unfollowList = new ArrayList<>();
        for (int i = 0; i < NUM_USERS; i++) {
            String alias = "@bulk_" + i;
            aliasList.add(alias);
            unfollowList.add(new Pair<>(alias, FOLLOW_TARGET_ALIAS));
        }

        dynamoDaoFactory.getUserDAO().batchDelete(aliasList);
        dynamoDaoFactory.getUserDAO().changeFollowersCount(FOLLOW_TARGET_ALIAS, -NUM_USERS);
        dynamoDaoFactory.getFollowDAO().batchUnfollow(unfollowList);

    }

    public static void find() {
        dynamoDaoFactory.getUserDAO().getUser("@bulk_1");
        dynamoDaoFactory.getFollowDAO().getFollowers(FOLLOW_TARGET_ALIAS, 25, null);
//        followDAO.isFollower(new User("", "", "@guy1", ""), new User("", "", "@emma", ""));
    }

    private static TweeterUsers makeFakeUser(int number){
        String firstName = "bulk_first_" + number;
        String lastName = "bulk_last_ " + number;
        String alias = "@bulk_" + number;
        String passwordHash = "$2a$10$HXk/fG3dj2J/zbrSGGnxv.RSGWOoWUCUHl1sCJYy2xEqjpbM7lbsO"; // password = 'test10'
        String imageURL = FOLLOW_TARGET_IMAGE_URL;
        int followersCount = 0;
        int followingCount = 1;

        // Note that in this example, a UserDTO only has a name and an alias.
        // The url for the profile image can be derived from the alias in this example
        TweeterUsers newUser = new TweeterUsers();
        newUser.setAlias(alias);
        newUser.setFirstName(firstName);
        newUser.setLastName(firstName);
        newUser.setImageURL(imageURL);
        newUser.setPasswordHash(passwordHash);
        newUser.setFollowersCount(followersCount);
        newUser.setFollowingCount(followingCount);

        return newUser;
    }

    private static TweeterFollows makeFakeFollow(int number){
        String followerFirstName = "bulk_first_" + number;
        String followerLastName = "bulk_last_ " + number;
        String followerAlias = "@bulk_" + number;
        String followerImageURL = FOLLOW_TARGET_IMAGE_URL;

        TweeterFollows newFollow = new TweeterFollows();
        newFollow.setFollowerAlias(followerAlias);
        newFollow.setFollowerFirstName(followerFirstName);
        newFollow.setFollowerLastName(followerLastName);
        newFollow.setFollowerImageURL(followerImageURL);
        newFollow.setFolloweeAlias(FOLLOW_TARGET_ALIAS);
        newFollow.setFolloweeFirstName(FOLLOW_TARGET_FIRST_NAME);
        newFollow.setFolloweeLastName(FOLLOW_TARGET_LAST_NAME);
        newFollow.setFolloweeImageURL(FOLLOW_TARGET_IMAGE_URL);

        return newFollow;
    }
}


