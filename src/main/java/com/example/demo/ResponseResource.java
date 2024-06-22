package com.example.demo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;

@Path("/evenValue")
@RequestScoped
public class ResponseResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{numVal}")
    public Response testValue(@PathParam("numVal") Integer val){
        if(val % 2 == 0){
            return Response.ok("The number is even").build();
        } else {
            return Response.notAcceptable(Collections.emptyList()).build();
        }
    }
}
