package org.esteban.services;

import org.esteban.mappers.S3EventMapper;
import org.esteban.models.S3EventModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.esteban.repositories.S3EventRepository;

@Service
public class S3EventService {
    @Value("${aws.sqs.s3events.url}")
    private String awsSqsS3eventsUrl;

    private final S3EventRepository repository;
    private final SqsMessageSenderService sqsMessageSenderService;
    private final S3EventMapper S3EventMapper;

    public S3EventService(S3EventRepository repository,
                          SqsMessageSenderService sqsMessageSenderService,
                          S3EventMapper S3EventMapper) {
        this.repository = repository;
        this.sqsMessageSenderService = sqsMessageSenderService;
        this.S3EventMapper = S3EventMapper;
    }

    public Flux<S3EventModel> findByBucketName(String bucketName) {
        return this.repository.findByBucketName(bucketName);
    }

    public Mono<S3EventModel> create(S3EventModel entity) {
        return repository.insert(entity)
                .doOnNext(result -> sendSqsMessage(result));
    }

    private void sendSqsMessage(S3EventModel entity) {
        sqsMessageSenderService.sendMessage(awsSqsS3eventsUrl, S3EventMapper.toS3EventMessageJson(entity))
                .onErrorResume(ex -> {
                    System.out.println("Error sending event message to SQS: " + ex.getMessage()); // it could be logged here
                    return Mono.empty();
                });

    }
}
