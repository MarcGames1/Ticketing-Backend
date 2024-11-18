package Ticket.DTO;

import Entities.Attachment;
import Enums.TaskStatus;

import java.util.List;

public class TicketDTO {
    public Long id;
    public String title;
    public String content;
    public TaskStatus status;
    public List<TicketTaskDTO> tasks;
    public List<Attachment> attachments;
}
