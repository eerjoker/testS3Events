package org.esteban.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)

    public Mono<ResponseEntity<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Argument error: " + ex.getMessage())
        );
    }

    @ExceptionHandler(AmazonServiceException.class)
    public Mono<ResponseEntity<String>> handleAmazonServiceException(AmazonServiceException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("Amazon Service error: " + ex.getMessage())
        );
    }

    @ExceptionHandler(AmazonClientException.class)
    public Mono<ResponseEntity<String>> handleAmazonClientException(AmazonClientException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("Amazon connection error: " + ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server error: " + ex.getMessage()));
    }
}
