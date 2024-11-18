package Task.DTO;

import jakarta.validation.constraints.NotNull;

public class AssignUserToTaskDTO {
    @NotNull
    public Long userId;
}
