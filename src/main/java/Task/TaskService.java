package Task;

import Entities.*;
import Enums.EmployeeRole;
import Enums.TaskStatus;
import Mapper.TaskMapper;
import Mapper.TaskStatusMapper;
import Shared.DTO.ChangeStatusDTO;
import Task.DTO.AssignUserToTaskDTO;
import Task.DTO.CreateTaskDTO;
import Task.DTO.TaskDTO;
import Task.DTO.UpdateTaskDTO;
import Utils.CurrentRequestData;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAllowedException;
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

    @Inject
    private CurrentRequestData currentRequestData;

    public Long create(CreateTaskDTO dto, Long ticketId) {
        var user = entityManager.find(User.class, dto.getUserId());
        if(user == null)
            throw new NotFoundException("User not found");
        var ticket = entityManager.find(Ticket.class, ticketId);
        if(ticket == null)
            throw new NotFoundException("Ticket not found");
        Task model = TaskMapper.INSTANCE.create(dto);
        model.setUser(user);
        model.setTicket(ticket);
        model.setStatus(TaskStatus.Pending);
        entityManager.persist(model);
        entityManager.flush();
        entityManager.clear();
        return model.getId();
    }

    public TaskDTO getById(Long id, Long ticketId) {
        QTask task = QTask.task;
        QTicket ticket = QTicket.ticket;
        QAttachment attachment = QAttachment.attachment;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var user = currentRequestData.getUser();
        var model = query.select(task)
                .from(task)
                .leftJoin(task.attachments, attachment)
                .join(task.ticket, ticket)
                .where(task.id.eq(id))
                .where(task.ticket.id.eq(ticketId))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Task not found");

        if(user.getRole() == EmployeeRole.EMPLOYEE && !Objects.equals(model.getUserId(), user.getId())){
            throw new ForbiddenException("This task doesn't belong to you");
        }
        return TaskMapper.INSTANCE.get(model);
    }

    public List<TaskDTO> getAll(Long ticketId){
        QTask task = QTask.task;
        QTicket ticket = QTicket.ticket;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var result = query.select(task)
                .from(task)
                .where(Optional.ofNullable(ticketId)
                        .map(task.ticket.id::eq)
                        .orElse(null))
                //.where(Optional.ofNullable(currentRequestData.getUser().getId())
                //        .map(task.user.id::eq)
                //        .orElse(null))
                .join(task.ticket, ticket)
                .fetch();
        return TaskMapper.INSTANCE.getAll(result);
    }

    public void changeStatus(Long id, ChangeStatusDTO dto) {
        var desiredStatus = TaskStatusMapper.INSTANCE.stringToEnum(dto.status);
        JPAQuery<?> query = new JPAQuery<Task>(entityManager);
        var model = query.select(QTask.task)
                .from(QTask.task)
                .where(QTask.task.id.eq(id))
                .fetchOne();

        if(model == null) {
            throw  new NotFoundException("Task not found");
        }

        var user = currentRequestData.getUser();

        if(user.getRole() == EmployeeRole.EMPLOYEE && !Objects.equals(model.getUserId(), user.getId())){
            throw new ForbiddenException("You can't change the status of this task");
        }

        model.setStatus(desiredStatus);

        entityManager.merge(model);
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
        QAttachment attachment = QAttachment.attachment;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        queryBuilder.delete(attachment)
                .where(attachment.task.id.eq(id))
                .execute();
        var res = queryBuilder.delete(task)
                .where(task.id.eq(id))
                .execute();
        entityManager.flush();
        entityManager.clear();
        return res;
    }

    public void assignUserToTask(Long taskId, AssignUserToTaskDTO dto){
        QTask task = QTask.task;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        var model = queryBuilder.select(task)
                .from(task)
                .where(task.id.eq(taskId))
                .fetchOne();

        if(model == null)
            throw new NotFoundException("Task not found");

        QUser qUser = QUser.user;
        var user = queryBuilder.select(qUser)
                .from(qUser)
                .where(qUser.id.eq(dto.userId))
                .fetchOne();

        if(user == null)
            throw new NotFoundException("User not found");

        model.setUser(user);
        entityManager.merge(model);
        entityManager.flush();
        entityManager.clear();
    }
}
