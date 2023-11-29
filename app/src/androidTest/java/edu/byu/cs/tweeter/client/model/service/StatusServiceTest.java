package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

class StatusServiceTest {
    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    /**
     * Create a StatusService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        // Setup an observer for the FollowService
        observer = new StatusServiceObserver();

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    /**
     * A {@link PagedObserver<Status>} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link StatusService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class StatusServiceObserver implements PagedObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> statuses;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void getSucceeded(List<Status> statuses, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.statuses = statuses;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleExceptions(String message) {
            this.success = false;
            this.message = null;
            this.statuses = null;
            this.hasMorePages = false;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * A {@link PagedHandler<Status>} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link StatusService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class StatusServiceHandler extends PagedHandler<Status> {
        public StatusServiceHandler(PagedObserver<Status> observer, String taskDescription, String key) {
            super(observer, taskDescription, key);
        }
    }

    /**
     * Verify that for successful requests, the {@link StatusService#getStory}
     * asynchronous method eventually returns the same result as the {@link ServerFacade}.
     */
    @Test
    public void testgetStory_validRequest_correctResponse() throws InterruptedException {
//        StatusServiceHandler statusServiceHandler = new StatusServiceHandler(observer, "description", "key");
//        GetStoryTask task = new GetStoryTask(currentAuthToken, currentUser, currentUser, 3, null, statusServiceHandler);
//        Mockito.doReturn(task).when(statusServiceSpy).getGetStoryTask(currentAuthToken, currentUser, currentUser, 3, null, observer);

        statusServiceSpy.getStory(currentAuthToken, currentUser, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getFakeStatuses().subList(0, 3);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertEquals(expectedStatuses, observer.getStatuses());
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }

    /**
     * Verify that for successful requests, the the {@link StatusService#getStory}
     * method loads correct number of statuses as defined by the given limit.
     */
    @Test
    public void testgetStory_validRequest_loadsProfileImages() throws InterruptedException {
        statusServiceSpy.getStory(currentAuthToken, currentUser, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> statuses = observer.getStatuses();
        Assertions.assertEquals(3, statuses.size());
    }

    /**
     * Verify that for unsuccessful requests, the the {@link StatusService#getStory}
     * method returns the same failure response as the server facade.
     */
    @Test
    public void testgetStory_invalidRequest_returnsNoFollowees() throws InterruptedException {
        statusServiceSpy.getStory(null, null, null, 0, null, observer);
        awaitCountDownLatch();

        Assertions.assertFalse(observer.isSuccess());
        Assertions.assertEquals(null, observer.getMessage());
        Assertions.assertNull(observer.getStatuses());
        Assertions.assertFalse(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }
}