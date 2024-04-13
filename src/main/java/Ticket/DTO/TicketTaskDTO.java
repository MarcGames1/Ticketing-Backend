package Ticket.DTO;

import Enums.TaskStatus;
import Shared.DTO.UserDTO;

public class TicketTaskDTO {
    public Long id;
    public String title;
    public String description;
    public TaskStatus status;
    public UserDTO user;
}
