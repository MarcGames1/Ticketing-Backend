package Ticket;
import Entities.*;

import Enums.EmployeeRole;
import Enums.TaskStatus;
import Mapper.TaskStatusMapper;
import Mapper.TicketMapper;
import Shared.DTO.ChangeStatusDTO;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.TicketDTO;
import Ticket.DTO.TicketsByStatus;
import Ticket.DTO.UpdateTicketDTO;
import Utils.CurrentRequestData;
import com.example.demo.awsServices.Mailer;
import com.example.demo.awsServices.S3Uploader;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Stateless
@Transactional
public class TicketService {
    public TicketService() {}
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private CurrentRequestData currentRequestData;

    @Inject
    private Mailer mailer;

    @Inject
    private S3Uploader s3;

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
        var user = currentRequestData.getUser();
        if(user.getRole() == EmployeeRole.EMPLOYEE && model.getTasks().stream().noneMatch(x -> Objects.equals(x.getUser().getId(), user.getId()))){
            throw new ForbiddenException("You can't see this ticket");
        }
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
                    .leftJoin(ticket.tasks, QTask.task)
                    .fetchJoin();
            case EMPLOYEE -> query.select(ticket)
                    .distinct()
                    .from(ticket)
                    .leftJoin(ticket.tasks, QTask.task)
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

    public void changeStatus(Long id, ChangeStatusDTO dto) {
        var desiredStatus = TaskStatusMapper.INSTANCE.stringToEnum(dto.status);
        JPAQuery<?> query = new JPAQuery<Ticket>(entityManager);
        var model = query.select(QTicket.ticket)
                .distinct()
                .from(QTicket.ticket)
                .leftJoin(QTicket.ticket.tasks, QTask.task).fetchJoin()
                .where(QTicket.ticket.id.eq(id))
                .fetchOne();
        if(model == null) {
            throw  new NotFoundException("Ticket not found");
        }

        var user = currentRequestData.getUser();

        if(user.getRole() == EmployeeRole.EMPLOYEE && model.getTasks().stream().noneMatch(x -> Objects.equals(x.getUser().getId(), user.getId()))){
            throw new ForbiddenException("You can't change the status of this ticket");
        }

        if(desiredStatus == TaskStatus.Completed){
            if(!model.getTasks().stream().allMatch(x -> x.getStatus() == TaskStatus.Completed)){
                throw new NotAllowedException("Ticket still have uncompleted tasks","",new String[]{});
            }

            model.setStatus(desiredStatus);
        } else {
            model.setStatus(desiredStatus);
        }
        entityManager.merge(model);

        try{
            for(var task : model.getTasks()){
                mailer.sendTicketStatusChangeEmail(task.getUser().getEmail(),model.getTitle(), model.getStatus());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Long delete(Long id) {
        QTicket ticket = QTicket.ticket;
        QTask task = QTask.task;
        QAttachment attachment = QAttachment.attachment;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        //TODO: Delete Attachments related to tasks and remove the files from the buckets for ticket & tasks attachments
        queryBuilder.delete(task)
                .where(task.ticket.id.eq(id))
                .execute();
        queryBuilder.delete(attachment)
                .where(attachment.ticket.id.eq(id))
                .execute();
        return queryBuilder.delete(ticket)
                .where(ticket.id.eq(id))
                .execute();
    }

    public void addAttachment(Long ticketId, InputStream file){
        QTicket qTicket = QTicket.ticket;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        var ticket = queryBuilder.select(qTicket)
                .from(qTicket)
                .where(qTicket.id.eq(ticketId))
                .fetchOne();
        if(ticket == null)
            throw new NotFoundException("Ticket not found");
        try{
            String fileId = UUID.randomUUID().toString();
            var jsonResponse = s3.uploadFile(fileId, file);
            var attachment = new Attachment(String.valueOf(jsonResponse.get("url")),fileId, ticket);
            entityManager.persist(attachment);
            entityManager.flush();
            entityManager.clear();
        } catch (Exception ignored){
            throw new BadRequestException("Something went wrong");
        }
    }

    public void removeAttachment(Long ticketId, Long attachmentId){
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        var attachment = queryBuilder.select(QAttachment.attachment)
                        .from(QAttachment.attachment)
                        .where(QAttachment.attachment.id.eq(attachmentId))
                        .where(QAttachment.attachment.ticket.id.eq(ticketId))
                        .fetchOne();
        if(attachment == null)
            throw new NotFoundException("Attachment not found");
        try{
            if(s3.fileExists(attachment.getS3Id())){
                s3.deleteFile(attachment.getS3Id());
                queryBuilder.delete(QAttachment.attachment)
                        .where(QAttachment.attachment.ticket.id.eq(ticketId))
                        .where(QAttachment.attachment.id.eq(attachmentId))
                        .execute();
                entityManager.flush();
                entityManager.clear();
            }
        } catch (Exception ignored){
            throw new BadRequestException("Something went wrong");
        }
    }
}
