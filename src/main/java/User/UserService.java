package User;

import Entitys.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;
    //private EntityManagerFactory

    public UserService(){

    }

    public void createUser(User user) {
        entityManager.persist(user);
    }

    public User getUser(Long userId) {
        return entityManager.find(User.class, userId);
    }

    public void updateUser(User user) {
        entityManager.merge(user);
    }

    public void deleteUser(Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
