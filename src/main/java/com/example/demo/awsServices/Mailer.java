package com.example.demo.awsServices;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;


@ApplicationScoped
public class Mailer {

    private static Mailer instance;

    private AmazonSimpleEmailService sesClient;
    private static final Logger LOGGER = Logger.getLogger(Mailer.class.getName());
    @Inject
    private Credentials creds;


    public Mailer() {}


    @PostConstruct
    private void init() {
        LOGGER.info("Initializing SES Client");
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(creds.getAWSAccessKeyId(), creds.getAWSSecretKey());
            sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(creds.getRegion())
                    .build();
            LOGGER.info("SES Client initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize SES Client: " + e.getMessage());
        }
    }
    public static synchronized Mailer getInstance() {
        if (instance == null) {
            instance = new Mailer();
        }
        return instance;
    }

    // Status change email method
    public void sendStatusChangeEmail(String to, String ticketId, String status) {
        String subject = "Status Update for Ticket " + ticketId;
        String body = String.format("Hello,\n\nThe status of your ticket %s has been changed to: %s.", ticketId, status);

        sendEmail(to, subject, body);
    }

    // Password Reset ?? OPTIONAL IMPLEMENTATION
    public void sendPasswordResetCode(String to, String code) {
        String subject = "Your Password Reset Code";
        String body = "Your password reset code is: " + code;

        sendEmail(to, subject, body);
    }

    // Add New User
    public void sendNewUserWelcomeEmail(String to, String username) {
        String subject = "Welcome to Our Service!";
        String body = "Hi " + username + ",\n\nWelcome to our service. We are glad to have you with us.";

        sendEmail(to, subject, body);
    }

    // General Send Email Method
    private void sendEmail(String to, String subject, String body) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(body)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource(creds.getSenderEmail()); // SES EMAIL

            sesClient.sendEmail(request);
        } catch (Exception ex) {
            LOGGER.severe("The email was not sent. Error message: " + ex.getMessage());
        }
    }

    public void sendTestEmail(){
        try{

            this.sendEmail("javademo45@gmail.com", "TEST", "HELLO!!!!");
        } catch (Error e) {
            LOGGER.severe("The email was not sent. Error message: ");
            throw new Error("PLM");
        }

    }
}

