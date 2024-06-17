package Shared.DTO;

import jakarta.validation.constraints.NotEmpty;

public class ChangeStatusDTO {
    @NotEmpty
    public String status;
}
