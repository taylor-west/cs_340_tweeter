package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.models.DataPage;

public interface FollowDAO {
    void follow(User follower, User followee);
    void unfollow(String followerAlias, String followeeAlias);
    boolean isFollower(String followerAlias, String followeeAlias);
    DataPage<User> getFollowing(String followerAlias, int limit, String lastFolloweeAlias);
    DataPage<User> getFollowers(String followeeAlias, int limit, String lastFollowerAlias);
    List<String> getFollowersAliases(String followeeAlias);
}
