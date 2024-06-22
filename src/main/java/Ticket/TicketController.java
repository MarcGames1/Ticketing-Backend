package Ticket;
import Enums.EmployeeRole;
import Shared.DTO.ChangeStatusDTO;
import Ticket.DTO.*;
import Utils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;
import java.util.List;


@Path("/tickets")
@RequestScoped
public class TicketController {
    @Inject
    private TicketService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public List<TicketsByStatus> getAll(){
        return service.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response create(@Valid CreateTicketDTO dto) {
        var id = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(id)
                .build();
    }

    @GET
    @Path("/report")
    public List<BasicTicketDTO> getReport(){
        return service.getLiveReportData();
    }

    @GET
    @Path("/{id}")
    @Secured
    public TicketDTO getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @PATCH
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response update(@Valid UpdateTicketDTO dto){
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
    @Path("/{id}/attachments")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response addAttachment(@PathParam("id") Long ticketId,
                                  @FormDataParam("file") InputStream fileInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileMetaData){
        service.addAttachment(ticketId, fileInputStream);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/attachments/{attId}")
    @Secured(roles = {EmployeeRole.MANAGER, EmployeeRole.SUPERVISOR})
    public Response deleteAttachment(@PathParam("id") Long ticketId, @PathParam("attId") Long attachmentId){
        service.removeAttachment(ticketId, attachmentId);
        return Response.ok().build();
    }
}
