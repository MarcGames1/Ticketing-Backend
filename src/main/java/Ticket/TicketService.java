package Ticket;
import Entities.*;

import Mapper.TicketMapper;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.TicketDTO;
import Ticket.DTO.UpdateTicketDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
@Transactional
public class TicketService {
    public TicketService() {}
    @PersistenceContext
    private EntityManager entityManager;

    public Long create(CreateTicketDTO dto) {
        Ticket model = TicketMapper.INSTANCE.create(dto);
        entityManager.persist(model);
        return model.getId();
    }

    public TicketDTO getById(Long id) {
        QTicket ticket = QTicket.ticket;
        QAttachment attachment = QAttachment.attachment;
        QTask task = QTask.task;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(ticket)
                .from(ticket)
                .leftJoin(ticket.attachments, attachment)
                .leftJoin(ticket.tasks, task)
                .where(ticket.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Ticket not found");
        return TicketMapper.INSTANCE.get(model);
    }

    public List<TicketDTO> getAll(){
        QTicket ticket = QTicket.ticket;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var result = query.select(ticket)
                .from(ticket)
                .fetch();
        return TicketMapper.INSTANCE.getAll(result);
    }

    public void update(UpdateTicketDTO dto) {
        var model = entityManager.find(Ticket.class, dto.getId());
        if(model == null)
            throw new NotFoundException("Ticket not found");
        model = TicketMapper.INSTANCE.update(model, dto);
        entityManager.merge(model);
    }

    public Long delete(Long id) {
        QTicket ticket = QTicket.ticket;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        return queryBuilder.delete(ticket)
                .where(ticket.id.eq(id))
                .execute();
    }
}
