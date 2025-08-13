package org.esteban.controllers;

import org.esteban.models.S3EventModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.esteban.services.S3EventService;

@RestController
@RequestMapping("/api/v1/s3-events")
class S3EventController {
    private final S3EventService service;
    public S3EventController(S3EventService service) {
        this.service = service;
    }

    @PostMapping("/")
    public Mono<String> registerEvent(@RequestBody Mono<S3EventModel> body) {
        return body.flatMap(event -> service.create(event))
                .map(event -> event.getId());
    }

    @GetMapping("/{bucketName}")
    public Flux<S3EventModel> getEvents(@PathVariable String bucketName) {
        // how to handle if there are too many
        return service.findByBucketName(cleanS3BucketName(bucketName));
    }

    private String cleanS3BucketName(String bucketName) {
        if (bucketName == null) {
            throw new IllegalArgumentException("Bucket name cannot have null value.");
        }
        if (bucketName.length() < 3) {
            throw new IllegalArgumentException("Bucket name is too short.");
        }
        if (bucketName.length() > 63) {
            throw new IllegalArgumentException("Bucket name is too large.");
        }

        String cleanedName = bucketName.toLowerCase();
        cleanedName = cleanedName.replaceAll("[^a-z0-9.-]", "-") // replaces weird characters
                        .replaceAll("^[.-]+", "") // removes start with . or -
                        .replaceAll("[.-]+$", ""); // removes end with . or -
        System.out.println("Cleaned bucket name: " + cleanedName);
        return cleanedName;
    }
}
