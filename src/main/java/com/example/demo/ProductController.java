package com.example.demo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/products")
public class ProductController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts(){
        return List.of(new Product("Product1", 100), new Product("Product2", 150));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Product getProductById(@PathParam("id") Integer id,@QueryParam("price") Integer price){
        return new Product("Product " + id.toString(), price);
    }
}

