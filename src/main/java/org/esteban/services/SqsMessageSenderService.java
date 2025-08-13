package org.esteban.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsMessageSenderService {
    private final SqsAsyncClient sqsClient;

    public SqsMessageSenderService(@Autowired SqsAsyncClient sqsClient) {
        this.sqsClient = sqsClient; // it comes from a Bean with autowired
    }

    public Mono<Void> sendMessage(String url, String message) {
        SendMessageRequest request = SendMessageRequest.builder()
            .queueUrl(url)
            .messageBody(message)
            .build();

        return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                .doOnNext(response -> System.out.println("Message ID: " + response.messageId()))
                .then(); // the response isn't needed anymore
    }

}
