package Ticket;
import Entitys.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class TicketService {
    @PersistenceContext
    private EntityManager entityManager;
//    TODO

    public void createTicket (Ticket ticket) {entityManager.persist(ticket);}
}
