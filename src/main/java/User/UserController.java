package User;

import Entitys.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@RequestScoped
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") Long userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Similar methods for update and delete
}

