package Task;
import Entities.Task;
import Task.DTO.CreateTaskDTO;
import Task.DTO.UpdateTaskDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/tickets")
@RequestScoped
public class TaskController {
    @Inject
    private TaskService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getAll(@QueryParam("userId") Long userId, @QueryParam("ticketId") Long ticketId){
        return service.getAll(ticketId, userId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateTaskDTO dto) {
        var id = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(id)
                .build();
    }

    @GET
    @Path("/{id}")
    public Task getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @PATCH
    public Response update(@Valid UpdateTaskDTO dto){
        service.update(dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        var deletedCount = service.delete(id);
        return Response.ok(deletedCount).build();
    }
}
