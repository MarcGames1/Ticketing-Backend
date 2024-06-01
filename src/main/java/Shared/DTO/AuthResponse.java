package Shared.DTO;

import Entities.Department;
import Enums.EmployeeRole;

public class AuthResponse {
    public Long id;
    public String firstName;
    public String lastName;
    public EmployeeRole role;
    public String email;
    public Department department;
}
