package Ticket.DTO;

import Entities.Attachment;
import Enums.TaskStatus;
import Shared.DTO.UserDTO;

import java.util.List;

public class TicketTaskDTO {
    public Long id;
    public String title;
    public String description;
    public TaskStatus status;
    public UserDTO user;
    public List<Attachment> attachments;
}
