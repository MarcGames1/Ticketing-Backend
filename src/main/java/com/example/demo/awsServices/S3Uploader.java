package com.example.demo.awsServices;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

@ApplicationScoped
public class S3Uploader {

    private AmazonS3 s3Client;
    private String bucketName;
    @Inject
    private Credentials creds;

    // Constructor
    public S3Uploader() {}

    @PostConstruct
    private void init() {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(creds.getRegion())  // use region
                .build();
        this.bucketName = creds.getBucketName(); // Use Bucket Name
    }
    // Load S3 file and return URL from S3
    // Load S3 file and return URL from S3
    public HashMap<String, Object> uploadFile(String key, InputStream file) {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream"); // set default content type
        PutObjectRequest request = new PutObjectRequest(bucketName, key, file, metadata);
        request.setMetadata(metadata);
        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = s3Client.putObject(request);
            putObjectResult.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        var uploadData = new HashMap<String, Object>();

        String url = s3Client.getUrl(bucketName, key).toString();

        uploadData.put("url", url);
        uploadData.put("key", key);
        uploadData.put("putObjectResult", putObjectResult);

        return uploadData;
    }


    // helper method that checks if file exists
    public Boolean fileExists(String key) {
        return s3Client.doesObjectExist(bucketName, key);
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
