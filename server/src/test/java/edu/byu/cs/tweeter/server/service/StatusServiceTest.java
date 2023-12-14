package edu.byu.cs.tweeter.server.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void postStatus() {
        // stub out request.getAuthToken()
        // stub out request.getStatus()

        // mock StatusSqsClient
        // stub out StatusSqsClient.addMessageToQueue()
        // verify that StatusSqsClient.addMessageToQueue() is called with the correct params
    }
}