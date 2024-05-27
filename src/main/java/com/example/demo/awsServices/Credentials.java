package com.example.demo.awsServices;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Credentials {
    private static Credentials instance;
    private String region;
    private String email;
    private String bucketName;

    private String AWS_ACCESS_KEY_ID;
    private String AWS_SECRET_KEY;

    // Constructor private to prevent direct instanting
    public Credentials() {
        // Load variables from .env
        Dotenv dotenv = Dotenv.load();
        this.region = dotenv.get("AWS_REGION", "eu-west-3");
        this.email = dotenv.get("AWS_EMAIL", "javademo293@gmail.com");
        this.bucketName = dotenv.get("AWS_BUCKET_NAME", "s3BucketName");
        this.AWS_ACCESS_KEY_ID = dotenv.get("AWS_ACCESS_KEY_ID");
        this.AWS_SECRET_KEY = dotenv.get("AWS_SECRET_KEY");

        // TODO add more AWS credentials as necessary with getter methods
    }

    // singleton
    public static Credentials getInstance() {
        if (instance == null) {
            instance = new Credentials();
        }
        return instance;
    }

    public String getAWSAccessKeyId() {
        return AWS_ACCESS_KEY_ID;
    }

    public String getAWSSecretKey() {
        return AWS_SECRET_KEY;
    }
    public String getRegion() {
        return this.region;
    }

    public String getSenderEmail() {
        return this.email;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
