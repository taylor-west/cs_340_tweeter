package edu.byu.cs.tweeter.server.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.server.JsonSerializer;


public class StatusSqsClient {
    public static final String POSTS_Q_URL = "https://sqs.us-east-1.amazonaws.com/172849472790/tweeter_posts_queue_std_sqs";
    public static final String JOBS_Q_URL = "https://sqs.us-east-1.amazonaws.com/172849472790/tweeter_jobs_queue_std_sqs";

    public static void addMessageToQueue(Object objToSend, String queueUrl) {
//        System.out.println("in StatusSqsClient.addMessageToQueue with queueUrl: " + queueUrl + ", objToSend: " + objToSend);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(JsonSerializer.serialize(objToSend));
//        System.out.println("  SendMessageRequest created...");

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
        String msgId = send_msg_result.getMessageId();
//        System.out.println("  SendMessageResult msgId: " + msgId);
//        System.out.println("  exiting StatusSqsClient.addMessageToQueue...");
        return;
    }
}