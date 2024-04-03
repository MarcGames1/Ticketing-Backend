package User.DTO;

public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String identifier;
    private String password;
    private Long departmentId;
    private String role;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String firstName, String lastName, String identifier, String password, Long departmentId, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identifier = identifier;
        this.password = password;
        this.departmentId = departmentId;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
