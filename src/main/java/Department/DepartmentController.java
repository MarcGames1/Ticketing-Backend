package Department;
import Department.DTO.BasicDepartmentDTO;
import Department.DTO.CreateDepartmentDTO;
import Department.DTO.DepartmentDTO;
import Department.DTO.UpdateDepartmentDTO;
import Entities.Department;
import Enums.EmployeeRole;
import Utils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/departments")
@RequestScoped
public class DepartmentController {
    @Inject
    private DepartmentService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER})
    public List<DepartmentDTO> getAll(){
        return service.getAll();
    }

     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<BasicDepartmentDTO> getList(){
        return service.getList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //@Secured(roles = {EmployeeRole.MANAGER})
    public Response create(@Valid CreateDepartmentDTO dto) {
        var id = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(id)
                .build();
    }

    @GET
    @Path("/{id}")
    @Secured(roles = {EmployeeRole.MANAGER})
    public DepartmentDTO getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @PATCH
    @Secured(roles = {EmployeeRole.MANAGER})
    public Response update(@Valid UpdateDepartmentDTO dto){
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

