package com.example.demo.awsServices;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;


@Path("/uploads")
@RequestScoped
public class UploadController {

    @Inject
    private S3Uploader uploader;

    public UploadController(){}
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("name") String name,
                           @FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData) {

        String fileId = UUID.randomUUID().toString();


        try {
            var jsonResponse = uploader.uploadFile(fileId, fileInputStream);
            return Response.status(Response.Status.CREATED).entity(jsonResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }
}
