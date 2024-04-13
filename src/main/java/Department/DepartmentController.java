package Department;
import Department.DTO.CreateDepartmentDTO;
import Department.DTO.UpdateDepartmentDTO;
import Entities.Department;
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
    public List<Department> getAll(){
        return service.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateDepartmentDTO dto) {
        var id = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(id)
                .build();
    }

    @GET
    @Path("/{id}")
    public Department getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @PATCH
    public Response update(@Valid UpdateDepartmentDTO dto){
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

