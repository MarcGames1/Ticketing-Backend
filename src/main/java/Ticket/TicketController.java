package Ticket;

import Entitys.Ticket;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/tickets")
@RequestScoped


public class TicketController {
    @Inject
    private TicketService ticketService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crateTicket (Ticket ticket){
    ticketService.createTicket(ticket);
    return Response.status((Response.Status.CREATED)).build();
    }
}
