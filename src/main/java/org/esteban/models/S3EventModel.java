package org.esteban.models;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "s3Events")
public class S3EventModel {
    //region properties
    @Id
    private String id;
    private String bucketName;
    private String objectKey;
    private String eventType;
    private Date eventTime;
    private int objectSize;
    //endregion

}