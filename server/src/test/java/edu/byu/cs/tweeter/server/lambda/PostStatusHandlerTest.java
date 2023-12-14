package edu.byu.cs.tweeter.server.lambda;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostStatusHandlerTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void handleRequest_unitTest() {
        // mock StatusService.postStatus and verify that it is called when PostStatusHandler receives a request
    }

    @Test
    void handleRequest_integrationTest() {
        // create a DynamoStatusDAO
        // use the DynamoStatusDAO to get initial copy of the user's story

        // create status to be posted
        // create PostStatusRequest
        // hit the PostStatusHandler

        // verify that an SQS message is sent
        // verify that FollowFetcher is triggered with the correct information (??? don't know if possible due to async ???)

        // use the DynamoStatusDAO to get updated copy of the user's story
        // compare the two stories to make sure that the status was added
    }
}