package User;

import Entitys.Department;
import Entitys.QUser;
import Entitys.User;
import User.DTO.CreateUserDTO;
import Mapper.UserMapper;
import User.DTO.UpdateUserDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@ApplicationScoped
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    public UserService(){

    }

    public Long create(CreateUserDTO dto) {
        Department department = entityManager.find(Department.class, dto.getDepartmentId());
        if(department == null)
            throw new NotFoundException("Department not found");
        User user = UserMapper.INSTANCE.create(dto);
        user.setDepartment(department);
        entityManager.persist(user);
        return user.getId();
    }

    public User getById(Long id) {
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(user)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("User not found");
        return model;
    }

    public List<User> getAll(Long departmentId){
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(user)
                .from(user)
                .where(Optional.ofNullable(departmentId)
                        .map(user.department.id::eq)
                        .orElse(null))
                .fetch();
    }

    public void update(UpdateUserDTO dto) {
        var model = entityManager.find(User.class, dto.getId());
        if(model == null)
            throw new NotFoundException("User not found");
        if(dto.getDepartmentId() == null){
            model.setDepartment(null);
        } else if(!Objects.equals(dto.getDepartmentId(), model.getDepartmentId())){
            var department = entityManager.find(Department.class, dto.getDepartmentId());
            if(department == null)
                throw new NotFoundException("Department not found");
            model.setDepartment(department);
        }
        model = UserMapper.INSTANCE.update(model, dto);
        entityManager.merge(model);
    }

    public Long delete(Long id) {
        QUser user = QUser.user;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        return queryBuilder.delete(user)
                .where(user.id.eq(id))
                .execute();
    }
}
