package org.esteban.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@JsonSerialize
@Data
@AllArgsConstructor
public class S3EventMessageDto {
    private final String eventId;
    private final String bucketName;
    private final String objectKey;
    private final Date eventTime;
}
