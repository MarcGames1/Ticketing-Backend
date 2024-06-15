package Ticket;
import Entities.*;

import Enums.TaskStatus;
import Mapper.TicketMapper;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.TicketDTO;
import Ticket.DTO.TicketsByStatus;
import Ticket.DTO.UpdateTicketDTO;
import Utils.CurrentRequestData;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class TicketService {
    public TicketService() {}
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private CurrentRequestData currentRequestData;

    public Long create(CreateTicketDTO dto) {
        Ticket model = TicketMapper.INSTANCE.create(dto);
        model.setStatus(TaskStatus.Pending);
        entityManager.persist(model);
        return model.getId();
    }

    public TicketDTO getById(Long id) {
        QAttachment attachment = QAttachment.attachment;
        QTask task = QTask.task;
        JPAQuery<?> query = new JPAQuery<Ticket>(entityManager);
        var model = query.select(QTicket.ticket)
                .distinct()
                .from(QTicket.ticket)
                .leftJoin(QTicket.ticket.attachments, attachment).fetchJoin()
                .leftJoin(QTicket.ticket.tasks, task).fetchJoin()
                .where(QTicket.ticket.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Ticket not found");
        return TicketMapper.INSTANCE.get(model);
    }

    public List<TicketsByStatus> getAll(){
        var user = currentRequestData.getUser();
        var tickets = generateTicketQuery(user).fetch();

        var response = new ArrayList<TicketsByStatus>();
        for(var status : TaskStatus.values()){
            var item = new TicketsByStatus();
            item.status = status;
            item.tickets = TicketMapper.INSTANCE.getAll(tickets.stream()
                    .filter(x -> x.getStatus() == status)
                    .toList());
            response.add(item);
        }
        return response;
    }

    private JPAQuery<Ticket> generateTicketQuery(User user){
        QTicket ticket = QTicket.ticket;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);

        return switch (user.getRole()) {
            case MANAGER, SUPERVISOR -> query.select(ticket)
                    .distinct()
                    .from(ticket)
                    .join(ticket.tasks, QTask.task)
                    .fetchJoin();
            case EMPLOYEE -> query.select(ticket)
                    .distinct()
                    .from(ticket)
                    .join(ticket.tasks, QTask.task)
                    .where(QTask.task.user.eq(user)).fetchJoin();
        };
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
