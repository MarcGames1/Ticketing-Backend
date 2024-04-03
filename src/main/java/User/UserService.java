package User;

import Entitys.Department;
import Entitys.User;
import User.DTO.CreateUserDTO;
import User.Mapper.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    public UserService(){

    }

    public Long createUser(CreateUserDTO dto) {
        Department department = entityManager.find(Department.class, dto.getDepartmentId());

        User user = UserMapper.INSTANCE.createDtoToModel(dto);
        user.setDepartment(department);
        entityManager.persist(user);
        return user.getId();
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
