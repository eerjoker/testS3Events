package org.esteban.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;

import java.net.URI;

@Configuration
public class AwsConfiguration {
    @Value("${aws.sqs.endpoint}")
    private String awsSqsEndpoint;

    @Value("${aws.sqs.region}")
    private Region awsSqsRegion;

    @Bean
    public SqsAsyncClient sqsClient() {
        // beans only run once at the start of the application
        SqsAsyncClientBuilder builder = awsSqsRegion == null ?
                                        SqsAsyncClient.builder() :
                                        SqsAsyncClient.builder().region(awsSqsRegion);

        if (awsSqsEndpoint != null && !awsSqsEndpoint.isBlank()) {
            builder = builder.endpointOverride(URI.create(awsSqsEndpoint)); // for local tests or specific endpoints
        }
        return builder.build();
    }
}
