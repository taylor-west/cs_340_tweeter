package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;

import java.util.List;

import edu.byu.cs.tweeter.client.presenter.AuthentificationPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.Status;

class IntegrationTest {
    @Test
    public void testPostStatus() throws InterruptedException {
        // mock only the view interface instead of the entire LoginFragment
        AuthentificationPresenter.AuthentificationView mockAuthentificationView = Mockito.mock(AuthentificationPresenter.AuthentificationView.class);

        // spy the LoginPresenter
        LoginPresenter loginPresenter = Mockito.spy(new LoginPresenter(mockAuthentificationView));

        // login
        loginPresenter.login("@test3", "test3");
        wait(3000);

        // mock the main activity
        MainActivity mainActivity = Mockito.mock(MainActivity.class);

        // spy the main presenter
        User currUser = Cache.getInstance().getCurrUser();

        // get user / authtoken info
        AuthToken currAuthToken = Cache.getInstance().getCurrUserAuthToken();
        MainPresenter mainPresenter = Mockito.spy(new MainPresenter(currUser, mainActivity));


        // post status to story
        String statusPostMessage = "integration test - status post message";
        mainPresenter.onStatusPosted(statusPostMessage, currUser, currAuthToken);
        Mockito.verify(mainActivity).showInfoMessage("Posting Status...");
        wait(4000);


        // mock the story view
        StoryFragment storyView = Mockito.mock(StoryFragment.class);
        // spy the story presenter
        StoryPresenter storyPresenter = Mockito.spy(new StoryPresenter(storyView, currUser, currAuthToken));

        // get story
        ArgumentCaptor<List<Status>> statusCaptor = ArgumentCaptor.forClass(List.class);
        storyPresenter.loadMoreItems();
        wait(5000);

        // verify that the first status in the story is the one we just posted
        Mockito.verify(storyPresenter).getSucceeded(statusCaptor.capture(), Mockito.anyBoolean());
        List<Status> statusesList = statusCaptor.getValue();
        Status firstPostedStatus = statusesList.get(0);
        Assertions.assertEquals(firstPostedStatus.getUser(), currUser);
        Assertions.assertEquals(firstPostedStatus.getPost(), statusPostMessage);
    }

    private void wait(int time) throws InterruptedException {
        Thread.sleep(time);
    }

}