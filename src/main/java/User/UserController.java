package User;

import Entities.User;
import Enums.EmployeeRole;
import Shared.DTO.UserDTO;
import User.DTO.CreateUserDTO;
import User.DTO.FullUserDTO;
import User.DTO.UpdateUserDTO;
import Utils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@RequestScoped
public class UserController {
    @Inject
    private UserService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER})
    public List<FullUserDTO> getAll(@QueryParam("departmentId") Long departmentId){
        return service.getAll(departmentId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public List<UserDTO> getAllList(){
        return service.getAllUsersList();
    }

    //@POST
    //@Consumes(MediaType.APPLICATION_JSON)
    //public Response create(@Valid CreateUserDTO dto) {
    //    var id = service.create(dto);
    //    return Response.status(Response.Status.CREATED)
    //            .entity(id)
    //            .build();
    //}

    @GET
    @Path("/{id}")
    @Secured(roles = {EmployeeRole.MANAGER})
    public FullUserDTO getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @PATCH
    @Secured(roles = {EmployeeRole.MANAGER})
    public Response update(@Valid UpdateUserDTO dto){
        service.update(dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Secured(roles = {EmployeeRole.MANAGER})
    public Response delete(@PathParam("id") Long id){
        var deletedCount = service.delete(id);
        return Response.ok(deletedCount).build();
    }
}