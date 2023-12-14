package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.models.DataPage;

public interface StatusDAO {
    DataPage<Status> getStory(User targetUser, int limit, Status lastStatus);
    DataPage<Status> getFeed(String userAlias, int limit, Status lastStatus);
    void postStatus(Status status);
    void postToFeed(String feedOwnerAlias, Status status);
    void batchPostToFeed(List<String> feedOwnerAliases, Status status);
}
