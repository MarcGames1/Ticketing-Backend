package Shared.DTO;

import Entities.Department;
import Enums.EmployeeRole;
import User.DTO.UserDepartmentDTO;

public class AuthResponse {
    public Long id;
    public String firstName;
    public String lastName;
    public EmployeeRole role;
    public String email;
    public UserDepartmentDTO department;
    public String idToken;
    public String accessToken;
    public String refreshToken;
}
