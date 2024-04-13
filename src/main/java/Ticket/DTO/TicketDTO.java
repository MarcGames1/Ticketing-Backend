package Ticket.DTO;

import Entities.Attachment;

import java.util.List;

public class TicketDTO {
    public Long id;
    public String title;
    public String content;
    public List<TicketTaskDTO> tasks;
    public List<Attachment> attachments;
}
