package Ticket;
import Entitys.Ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class TicketService {
    public TicketService() {}
    @PersistenceContext
    private EntityManager entityManager;
//    TODO

    public void createTicket (Ticket ticket) {entityManager.persist(ticket);}
}
