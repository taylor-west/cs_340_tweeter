package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.observers.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class FollowServiceTest {

    private User currentUser;
    private AuthToken currentAuthToken;

    private FollowService followServiceSpy;
    private FollowServiceObserver observer;

    private CountDownLatch countDownLatch;

    /**
     * Create a FollowService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        followServiceSpy = Mockito.spy(new FollowService());

        // Setup an observer for the FollowService
        observer = new FollowServiceObserver();

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
     * A {@link PagedObserver<User>} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link FollowService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class FollowServiceObserver implements PagedObserver<User> {

        private boolean success;
        private String message;
        private List<User> followees;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void getSucceeded(List<User> followees, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.followees = followees;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.followees = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleExceptions(String message) {
            this.success = false;
            this.message = null;
            this.followees = null;
            this.hasMorePages = false;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<User> getFollowing() {
            return followees;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Verify that for successful requests, the {@link FollowService#getFollowing}
     * asynchronous method eventually returns the same result as the {@link ServerFacade}.
     */
    @Test
    public void testgetFollowing_validRequest_correctResponse() throws InterruptedException {
        followServiceSpy.getFollowing(currentAuthToken, currentUser, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<User> expectedFollowees = FakeData.getInstance().getFakeUsers().subList(0, 3);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertEquals(expectedFollowees, observer.getFollowing());
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }

    /**
     * Verify that for successful requests, the the {@link FollowService#getFollowing}
     * method loads the profile image of each user included in the result.
     */
    @Test
    public void testgetFollowing_validRequest_loadsProfileImages() throws InterruptedException {
        followServiceSpy.getFollowing(currentAuthToken, currentUser, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<User> followees = observer.getFollowing();
        Assertions.assertTrue(followees.size() > 0);
    }

    /**
     * Verify that for unsuccessful requests, the the {@link FollowService#getFollowing}
     * method returns the same failure response as the server facade.
     */
    @Test
    public void testgetFollowing_invalidRequest_returnsNoFollowees() throws InterruptedException {
        followServiceSpy.getFollowing(null, null, null, 0, null, observer);
        awaitCountDownLatch();

        Assertions.assertFalse(observer.isSuccess());
        Assertions.assertEquals("Failed to get following: null", observer.getMessage());
        Assertions.assertNull(observer.getFollowing());
        Assertions.assertFalse(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }
}
