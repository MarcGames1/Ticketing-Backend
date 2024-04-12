package Entitys;

import Enums.EmployeeRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email",unique = true)
    private String email;

    @Column (name= "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Enumerated(EnumType.ORDINAL)
    private EmployeeRole role;

    protected User () {}

    public User(Long id, String firstName, String lastName , String email, String password, EmployeeRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // TODO Hash password
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

    public Department getDepartment() {
        return department;
    }
    public Long getDepartmentId(){
        if(department == null) return null;
        return department.getId();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

