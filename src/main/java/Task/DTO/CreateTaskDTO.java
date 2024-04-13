package Task.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateTaskDTO {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    private Long userId;
    @NotNull
    private Long ticketId;
    public CreateTaskDTO(){}

    public CreateTaskDTO(String title, String description, Long userId, Long ticketId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
