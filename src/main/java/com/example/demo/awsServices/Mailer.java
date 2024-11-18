package com.example.demo.awsServices;
import Enums.TaskStatus;
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
    public void sendTicketStatusChangeEmail(String to, String ticketTitle, TaskStatus status) {
        String subject = "⚡\uFE0F Status Update for Ticket " + ticketTitle + " ⚡\uFE0F";
        String body = String.format("Hello,\n\nThe status of your ticket '%s' has been changed to: %s.", ticketTitle, status);

        sendEmail(to, subject, body);
    }

    public void sendTaskStatusChangeEmail(String to, String ticketTitle, String taskTitle,TaskStatus status) {
        String subject = "⚡\uFE0F Status Update for Task " + taskTitle + " of Ticket " + ticketTitle + " ⚡\uFE0F";
        String body = String.format("Hello,\n\nThe status of your task '%s' has been changed to: %s.", taskTitle, status);

        sendEmail(to, subject, body);
    }

    public void sendTaskAssignedEmail(String to, String ticketTitle, String taskTitle, String content) {
        String subject = "\uD83D\uDD25 New Assignment for you \uD83D\uDE0E";
        String body = String.format("Hi\n You have been assigned to a new task.\nTicket: %s\nTask: %s\n%s", ticketTitle, taskTitle, content);

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

