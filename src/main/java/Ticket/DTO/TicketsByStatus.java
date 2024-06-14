package Ticket.DTO;

import Enums.TaskStatus;

import java.util.List;

public class TicketsByStatus {
    public TaskStatus status;
    public List<TicketDTO> tickets;
}