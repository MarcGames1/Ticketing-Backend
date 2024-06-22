package Entities;

import Enums.EmployeeRole;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Singleton
@Startup
public class Seeds {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void seedDatabase() {
        seedDepartments();
        seedUsers();
    }

    @Transactional
    private void seedUsers(){
        Long usersCount = entityManager.createQuery("select COUNT(e) from User e", Long.class).getSingleResult();
        if(usersCount != 0) return;
        var admin = new User("Hamza", "Hazin", "hamza.hazin.personal@gmail.com", EmployeeRole.MANAGER);
        entityManager.persist(admin);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    private void seedDepartments(){
        Long departmentsCount = entityManager.createQuery("select COUNT(e) from Department e", Long.class).getSingleResult();
        if(departmentsCount != 0) return;
        var departments = List.of("IT", "HR");
        for(var deptName : departments){
            entityManager.persist(new Department(deptName));
        }
        entityManager.flush();
        entityManager.clear();
    }
}
