package com.example.demo;


import com.example.demo.awsServices.Mailer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.logging.Logger;

@Path("/test")
public class TestResource {
    @Inject
    private Mailer mailer;






    @GET
    @Produces(MediaType.TEXT_HTML)
    public String test() {
        try {
            this.mailer.sendTestEmail();
            return "Email sent successfully!";
        } catch (Exception e) {

            return "Failed to send email.";
        }
    }
}
