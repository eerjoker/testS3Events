package org.esteban.repositories;

import org.esteban.models.S3EventModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface S3EventRepository extends ReactiveMongoRepository<S3EventModel, String> {
    Flux<S3EventModel> findByBucketName(String bucketName);
}
