package edu.byu.cs.tweeter.server.sqs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusSqsClientTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void addMessageToQueue() {
        // mock a PostStatusRequest
        // tests queueUrl = POSTS_Q_URL

        // verify that the message is sent to the queue
        //   OR
        // mock AmazonSQS and stub sendMessage, verify that it is called correctly, then mock SendMessageResult and stub getMessageId
    }
}