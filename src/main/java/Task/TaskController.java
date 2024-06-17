package Task;
import Entities.Task;
import Enums.EmployeeRole;
import Shared.DTO.ChangeStatusDTO;
import Task.DTO.AssignUserToTaskDTO;
import Task.DTO.CreateTaskDTO;
import Task.DTO.TaskDTO;
import Task.DTO.UpdateTaskDTO;
import Utils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/tickets/{ticketId}/tasks")
@RequestScoped
public class TaskController {
    @Inject
    private TaskService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public List<TaskDTO> getAll(@PathParam("ticketId") Long ticketId){
        return service.getAll(ticketId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response create(@PathParam("ticketId") Long ticketId, @Valid CreateTaskDTO dto) {
        var id = service.create(dto, ticketId);
        return Response.status(Response.Status.CREATED)
                .entity(id)
                .build();
    }

    @GET
    @Path("/{id}")
    @Secured
    public TaskDTO getById(@PathParam("id") Long id, @PathParam("ticketId") Long ticketId) {
        return service.getById(id, ticketId);
    }

    @PATCH
    @Secured(roles = {EmployeeRole.MANAGER , EmployeeRole.SUPERVISOR})
    public Response update(@Valid UpdateTaskDTO dto){
        service.update(dto);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}/changeStatus")
    @Secured
    public Response changeStatus(@PathParam("id") Long id, @Valid ChangeStatusDTO dto){
        service.changeStatus(id, dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response delete(@PathParam("id") Long id){
        var deletedCount = service.delete(id);
        return Response.ok(deletedCount).build();
    }

    @POST
    @Path("/{id}/assignUser")
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response assignUserToTask(@PathParam("id") Long taskId, @Valid AssignUserToTaskDTO dto){
        service.assignUserToTask(taskId,dto);
        return Response.ok().build();
    }
}
