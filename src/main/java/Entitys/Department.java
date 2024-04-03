package Entitys;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "department")
    private List<User> users;

    public Department(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department() {
    }

    public String getName() {
        return name;
    }

    public Long getId() {return id;}
    public void setName(String name) {
        this.name = name;
    }


// TODO methods for users

}
