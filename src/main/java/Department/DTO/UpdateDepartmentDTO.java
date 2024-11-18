package Department.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateDepartmentDTO {
    @NotNull
    private  Long id;

    @NotEmpty
    private String name;

    public UpdateDepartmentDTO(){}

    public UpdateDepartmentDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
