package com.example.demo.awsServices;

import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
    private static Credentials instance;
    private String region;
    private String email;
    private String bucketName;

    // Constructor private to prevent direct instanting
    private Credentials() {
        // Load variables from .env
        Dotenv dotenv = Dotenv.load();
        this.region = dotenv.get("AWS_REGION", "eu-west-1");
        this.email = dotenv.get("AWS_EMAIL", "example@aws.com");
        this.bucketName = dotenv.get("AWS_BUCKET_NAME", "s3BucketName");
    }

    // singleton
    public static Credentials getInstance() {
        if (instance == null) {
            instance = new Credentials();
        }
        return instance;
    }

    public String getRegion() {
        return this.region;
    }

    public String getEmail() {
        return this.email;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
