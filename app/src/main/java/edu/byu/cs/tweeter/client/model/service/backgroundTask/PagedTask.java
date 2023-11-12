package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose following is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Maximum number of items to return (i.e., page size).
     */
    protected int limit;

    /**
     * The last item returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;
    protected Pair<List<T>, Boolean> pageOfItems;

    protected PagedTask(Handler messageHandler) {
        super(messageHandler);
    }
}
