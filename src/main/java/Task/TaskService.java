package Task;

import Entities.*;
import Enums.TaskStatus;
import Mapper.TaskMapper;
import Task.DTO.CreateTaskDTO;
import Task.DTO.TaskDTO;
import Task.DTO.UpdateTaskDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class TaskService {
    public TaskService() {}
    @PersistenceContext
    private EntityManager entityManager;

    public Long create(CreateTaskDTO dto) {
        var user = entityManager.find(User.class, dto.getUserId());
        if(user == null)
            throw new NotFoundException("User not found");
        var ticket = entityManager.find(Ticket.class, dto.getTicketId());
        if(ticket == null)
            throw new NotFoundException("Ticket not found");
        Task model = TaskMapper.INSTANCE.create(dto);
        model.setUser(user);
        model.setTicket(ticket);
        model.setStatus(TaskStatus.Pending);
        entityManager.persist(model);
        return model.getId();
    }

    public TaskDTO getById(Long id) {
        QTask task = QTask.task;
        QTicket ticket = QTicket.ticket;
        QAttachment attachment = QAttachment.attachment;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(task)
                .from(task)
                .join(task.attachments, attachment)
                .join(task.ticket, ticket)
                .where(task.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Task not found");
        return TaskMapper.INSTANCE.get(model);
    }

    public List<TaskDTO> getAll(Long ticketId, Long userId){
        QTask task = QTask.task;
        QTicket ticket = QTicket.ticket;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var result = query.select(task)
                .from(task)
                .where(Optional.ofNullable(ticketId)
                        .map(task.ticket.id::eq)
                        .orElse(null))
                .where(Optional.ofNullable(userId)
                        .map(task.user.id::eq)
                        .orElse(null))
                .join(task.ticket, ticket)
                .fetch();
        return TaskMapper.INSTANCE.getAll(result);
    }

    public void update(UpdateTaskDTO dto) {
        var model = entityManager.find(Task.class, dto.getId());
        if(model == null)
            throw new NotFoundException("Task not found");
        if(!Objects.equals(dto.getUserId(), model.getUserId())){
            var user = entityManager.find(User.class, dto.getUserId());
            if(user == null)
                throw new NotFoundException("User not found");
            model.setUser(user);
        }
        model = TaskMapper.INSTANCE.update(model, dto);
        entityManager.merge(model);
    }

    public Long delete(Long id) {
        QTask task = QTask.task;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        return queryBuilder.delete(task)
                .where(task.id.eq(id))
                .execute();
    }
}
