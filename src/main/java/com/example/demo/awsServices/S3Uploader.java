package com.example.demo.awsServices;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class S3Uploader {

    private AmazonS3 s3Client;
    private String bucketName;

    // Constructor
    public S3Uploader(Credentials credentials) {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(credentials.getRegion()) // Utilizează regiunea din obiectul Credentials
                .build();
        this.bucketName = credentials.getBucketName(); // Utilizează numele bucket-ului din obiectul Credentials
    }

    // Load S3 file and return URL from S3
    public String uploadFile(String key, File file) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());

        PutObjectRequest request = new PutObjectRequest(bucketName, key, stream, metadata);
        s3Client.putObject(request);

        return s3Client.getUrl(bucketName, key).toString();
    }


    // helper method that checks if file exists
    private Boolean fileExists(String key) {
        return s3Client.doesBucketExistV2(key);
    }
    public String getFileUrl(String key) {
        if( this.fileExists(key)) {
         return s3Client.getUrl(bucketName, key).toString();
        }
        else throw new IllegalArgumentException("File not found in S3 bucket.");

    }
    public void deleteFile(String key) {
        // Check if file exists and delete it
        if (this.fileExists(key)) {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        } else {
            throw new IllegalArgumentException("File not found in S3 bucket.");
        }
    }
}
