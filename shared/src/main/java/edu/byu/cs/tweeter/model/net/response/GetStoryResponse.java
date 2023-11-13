package edu.byu.cs.tweeter.model.net.response;


import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * A paged response for a {@link GetStoryRequest}.
 */
public class GetStoryResponse extends PagedResponse {

    private List<Status> statuses;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetStoryResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param statuses    the statuses to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public GetStoryResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;

        System.out.println("new GetStoryResponse is being constructed with statuses: " + getStatusesString());

    }

    /**
     * Returns the statuses for the corresponding request.
     *
     * @return the statuses.
     */
    public List<Status> getStatuses() {
        return statuses;
    }

    /**
     * Sets the statuses for the request.
     *
     */
    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetStoryResponse that = (GetStoryResponse) o;
        return Objects.equals(statuses, that.statuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses);
    }

    public String getStatusesString() {
        String s = "";
        for (Status status : statuses) {
            s += status.hashCode();
            if(status != statuses.get(statuses.size()-1)){
                s += ", ";
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return "GetStoryResponse{" +
                "statuses=" + getStatusesString() +
                '}';
    }
}
