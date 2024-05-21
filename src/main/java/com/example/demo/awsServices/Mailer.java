package com.example.demo.awsServices;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import java.util.Properties;

public class Mailer {

    private AmazonSimpleEmailService sesClient;

    public Mailer() {
        sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion("eu-west-1") // Choose corect region from AWS
                .build();
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
                    .withSource("your-email@example.com"); // SES EMAIL

            sesClient.sendEmail(request);
            System.out.println("Email sent to " + to);
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }
    }
}

// TODO Create SES account, credentials and test implementation
