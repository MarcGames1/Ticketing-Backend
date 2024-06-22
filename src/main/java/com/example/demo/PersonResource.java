package com.example.demo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/person")
@RequestScoped
public class PersonResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(){
        return new Person("Hamza", 24);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String handlePersonRequest(Person person){
        return person.toString();
    }
}
