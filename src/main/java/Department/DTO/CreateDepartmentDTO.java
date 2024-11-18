package Department.DTO;

import jakarta.validation.constraints.NotEmpty;

public class CreateDepartmentDTO {
    @NotEmpty
    private String name;

    public CreateDepartmentDTO(){}

    public CreateDepartmentDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
