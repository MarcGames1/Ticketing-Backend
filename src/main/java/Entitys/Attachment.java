package Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Attachment {
//    TODO Attachment with url and id from s3bucket
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "S3bucketId")
    private String s3Id;

    // Added s3bucket ID instead of name
    // TODO add name field if you think is necesary
}
