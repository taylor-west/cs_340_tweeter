package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

class ServerFacadeTest {
    private ServerFacade serverFacadeSpy;
    private String registerUrl = UserService.getRegisterUrlPath();

    /**
     * Create a mock ClientCommunicator that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
         serverFacadeSpy = Mockito.spy(ServerFacade.class);
    }

    @Test
    public void testregister_validRequest_correctResponse() {
        RegisterRequest request = new RegisterRequest("test_username", "test_password", "first_name", "last_name", "image/aaaaaaaa");

        try{
            RegisterResponse response = serverFacadeSpy.register(request, registerUrl);
            Assertions.assertNotNull(response);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(FakeData.getInstance().getFirstUser(),response.getUser());
            Assertions.assertEquals(FakeData.getInstance().getAuthToken(),response.getAuthToken());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    public void testregister_invalidRequest_doesNotReturnsSuccess() {
        RegisterRequest request = new RegisterRequest("test_username", null, "first_name", "last_name", "image/aaaaaaaa");

        try{
            RegisterResponse response = serverFacadeSpy.register(request, registerUrl);
            Assertions.assertNotNull(response);
            Assertions.assertFalse(response.isSuccess());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    public void testregister_invalidRequest_returnsNoUser() {
        RegisterRequest request = new RegisterRequest("test_username", null, "first_name", "last_name", "image/aaaaaaaa");

        try{
            RegisterResponse response = serverFacadeSpy.register(request, registerUrl);
            Assertions.assertNotNull(response);
            Assertions.assertNull(response.getUser());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    public void testregister_invalidRequest_returnsNoAuthToken() {
        RegisterRequest request = new RegisterRequest("test_username", null, "first_name", "last_name", "image/aaaaaaaa");

        try{
            RegisterResponse response = serverFacadeSpy.register(request, registerUrl);
            Assertions.assertNotNull(response);
            Assertions.assertNull(response.getAuthToken());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowing_validRequest_correctResponse() {
        int limit = 5;
        AuthToken authToken = new AuthToken();
        FollowingRequest request = new FollowingRequest(authToken, "currUserAlias", limit, null, "test_follower_alias");
        String followingUrl = FollowService.getFollowingUrlPath("currUserAlias");
        try{
            FollowingResponse reponse = serverFacadeSpy.getFollowing(request, followingUrl);
            Assertions.assertTrue(reponse.isSuccess());
            Assertions.assertNotNull(reponse.getFollowing());
            Assertions.assertEquals(request.getLimit(), reponse.getFollowing().size());

            Assertions.assertArrayEquals(FakeData.getInstance().getFakeUsers().subList(0, request.getLimit()).toArray(), reponse.getFollowing().toArray());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowing_invalidLimit_returnsNoFollowing() {
        int limit = 0;
        AuthToken authToken = new AuthToken();
        FollowingRequest request = new FollowingRequest(authToken, "currUserAlias", limit, null, "test_follower_alias");
        String followingUrl = FollowService.getFollowingUrlPath("currUserAlias");

        try{
            FollowingResponse reponse = serverFacadeSpy.getFollowing(request, followingUrl);
            Assertions.assertFalse(reponse.isSuccess());
            Assertions.assertNull(reponse.getFollowing());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowing_invalidFollowerAlias_returnsNoFollowing() {
        int limit = 5;
        AuthToken authToken = new AuthToken();
        FollowingRequest request = new FollowingRequest(authToken, "currUserAlias", limit, null, null);
        String followingUrl = FollowService.getFollowingUrlPath("currUserAlias");

        try{
            FollowingResponse reponse = serverFacadeSpy.getFollowing(request, followingUrl);
            Assertions.assertFalse(reponse.isSuccess());
            Assertions.assertNull(reponse.getFollowing());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }


    @Test
    void testgetFollowers_validRequest_correctResponse() {
        int limit = 5;
        AuthToken authToken = new AuthToken();
        FollowersRequest request = new FollowersRequest(authToken, "currUserAlias", limit, null, "test_followee_alias");
        String followersUrl = FollowService.getFollowersUrlPath("currUserAlias");
        try{
            FollowersResponse reponse = serverFacadeSpy.getFollowers(request, followersUrl);
            Assertions.assertTrue(reponse.isSuccess());
            Assertions.assertNotNull(reponse.getFollowers());
            Assertions.assertEquals(request.getLimit(), reponse.getFollowers().size());

            Assertions.assertArrayEquals(FakeData.getInstance().getFakeUsers().subList(0, request.getLimit()).toArray(), reponse.getFollowers().toArray());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowers_invalidFolloweeAlias_returnsNoFollowers() {
        int limit = 5;
        AuthToken authToken = new AuthToken();
        FollowersRequest request = new FollowersRequest(authToken, "currUserAlias", limit, null, null);
        String followersUrl = FollowService.getFollowersUrlPath("currUserAlias");

        try{
            FollowersResponse reponse = serverFacadeSpy.getFollowers(request, followersUrl);
            Assertions.assertFalse(reponse.isSuccess());
            Assertions.assertNull(reponse.getFollowers());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }


    @Test
    void testgetFollowersCount_validRequest_correctResponse() {
        AuthToken authToken = new AuthToken();
        GetFollowersCountRequest request = new GetFollowersCountRequest(authToken, "currUserAlias", "test_followee_alias");
        String followersCountUrl = FollowService.getFollowersCountUrlPath("currUserAlias");
        try{
            GetFollowersCountResponse reponse = serverFacadeSpy.getFollowersCount(request, followersCountUrl);
            Assertions.assertTrue(reponse.isSuccess());
            Assertions.assertNotNull(reponse.getFollowersCount());
            Assertions.assertEquals(FakeData.getInstance().getFakeUsers().size(), reponse.getFollowersCount());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowersCount_invalidRequest_returnsZeroFollowersCount() {
        AuthToken authToken = new AuthToken();
        GetFollowersCountRequest request = new GetFollowersCountRequest(authToken, "currUserAlias", null);
        String followersCountUrl = FollowService.getFollowersCountUrlPath("currUserAlias");
        try{
            GetFollowersCountResponse reponse = serverFacadeSpy.getFollowersCount(request, followersCountUrl);
            Assertions.assertFalse(reponse.isSuccess());
            Assertions.assertEquals(0, reponse.getFollowersCount());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }

    @Test
    void testgetFollowingCount_validRequest_correctResponse() {
        AuthToken authToken = new AuthToken();
        GetFollowingCountRequest request = new GetFollowingCountRequest(authToken, "currUserAlias", "test_follower_alias");
        String followingCountUrl = FollowService.getFollowingCountUrlPath("currUserAlias");
        try{
            GetFollowingCountResponse reponse = serverFacadeSpy.getFollowingCount(request, followingCountUrl);
            Assertions.assertTrue(reponse.isSuccess());
            Assertions.assertNotNull(reponse.getFolloweesCount());
            Assertions.assertEquals(FakeData.getInstance().getFakeUsers().size(), reponse.getFolloweesCount());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }
    @Test
    void testgetFollowingCount_invalidRequest_returnsZeroFolloweesCount() {
        AuthToken authToken = new AuthToken();
        GetFollowingCountRequest request = new GetFollowingCountRequest(authToken, "currUserAlias", null);
        String followingCountUrl = FollowService.getFollowingCountUrlPath("currUserAlias");
        try{
            GetFollowingCountResponse reponse = serverFacadeSpy.getFollowingCount(request, followingCountUrl);
            Assertions.assertFalse(reponse.isSuccess());
            Assertions.assertEquals(0, reponse.getFolloweesCount());
        }catch (IOException | TweeterRemoteException e){
            Assertions.fail();
        }
    }
}