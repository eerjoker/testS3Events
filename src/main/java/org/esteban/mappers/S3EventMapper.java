package org.esteban.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.esteban.dto.S3EventMessageDto;
import org.esteban.models.S3EventModel;
import org.springframework.stereotype.Component;

@Component
public class S3EventMapper {
    private final ObjectMapper objectMapper;
    public S3EventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public S3EventMessageDto toS3EventMessageDto(S3EventModel entity) {
        return new S3EventMessageDto(entity.getId(),
            entity.getBucketName(),
            entity.getObjectKey(),
            entity.getEventTime());
    }

    public String toS3EventMessageJson(S3EventModel entity) {
        try {
            return objectMapper.writeValueAsString(toS3EventMessageDto(entity));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unexpected error when creating the JSON message for the S3 event: " + ex.getMessage());
        }
    }
}
