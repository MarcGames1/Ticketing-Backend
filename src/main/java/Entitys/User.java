package Entitys;

import Enums.EmployeeRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column (name= "password")
    private String password;
    @Column (name = "departmentId")
    private String departmentId;

    @Column (name = "role")
    private EmployeeRole role;

    protected User () {}



    public User(Long id, String firstName, String lastName , String email, String password, String departmentId, EmployeeRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // TODO Hash password
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

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private boolean isCorrectPassword (String inputPassword) {
//        TODO Method that compares the inputPassword to the Hashed Password in DB and
        // returns a bool
        return true;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public EmployeeRole getRole() {
        return role;
    }
    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

