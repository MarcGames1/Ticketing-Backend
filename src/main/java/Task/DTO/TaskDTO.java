package Task.DTO;

import Entities.Attachment;
import Enums.TaskStatus;
import Shared.DTO.UserDTO;

import java.util.List;

public class TaskDTO {
    public Long id;
    public TaskStatus status;
    public String title;
    public String description;
    public String trackingId;
    public UserDTO user;
    public TaskTicketDTO ticket;
    public List<Attachment> attachments;
}