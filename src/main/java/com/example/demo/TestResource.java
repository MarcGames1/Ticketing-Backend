package com.example.demo;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import test.TestController;

import java.util.HashMap;
import java.util.Map;

@Path("/test")
@RequestScoped
public class TestResource {
    @Inject
    private TestController controller;






    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        try {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("success", true)
                    .add("message", "Success!")
                    // Adaugă alte valori după necesități
                    .build();
            throw new Exception("x");
//            return Response.ok(jsonObject).build();


        } catch (Exception e) {

            JsonObject errorObject = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Failed to perform operation.")
                    .add("error", e.getMessage())
                    .build();
           return  Response.serverError().entity(errorObject).build();
        }

    }
}


