package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeedsRequest extends AuthenticatedRequest {
    private List<String> followerAliases;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private UpdateFeedsRequest() {}

    /**
     * Creates an instance.
     *
     * @param followerAliases a list of follower aliases to whose feeds the status should be posted.
     * @param status the status that will be posted to the feeds.
     */
    public UpdateFeedsRequest(List<String> followerAliases, Status status) {
        this.followerAliases = followerAliases;
        this.status = status;
    }

    public List<String> getFollowerAliases() {
        return followerAliases;
    }

    public void setFollowerAliases(List<String> followerAliases) {
        this.followerAliases = followerAliases;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateFeedsRequest{" +
                "status={" + status.getUser() + " - '" + status.getPost() + "'}" +
                ", followerAliases=[" + followerAliases.get(0) + " ... " + followerAliases.get(followerAliases.size()-1) + "]" +
                '}';
    }
}
