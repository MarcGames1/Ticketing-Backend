package User;

import Entities.Department;
import Entities.QUser;
import Entities.User;
import Shared.DTO.UserDTO;
import User.DTO.CreateUserDTO;
import Mapper.UserMapper;
import User.DTO.FullUserDTO;
import User.DTO.UpdateUserDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Stateless
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    public UserService(){

    }

    public Long create(CreateUserDTO dto) {
        User user = UserMapper.INSTANCE.create(dto);
        if(dto.getDepartmentId() != null){
            Department department = entityManager.find(Department.class, dto.getDepartmentId());
            if(department == null)
                throw new NotFoundException("Department not found");
            user.setDepartment(department);
        }
        entityManager.persist(user);
        return user.getId();
    }

    public FullUserDTO getById(Long id) {
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(user)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("User not found");
        return UserMapper.INSTANCE.full(model);
    }

    public User getByIdRaw(Long id) {
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

    public List<FullUserDTO> getAll(Long departmentId){
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var res = query.select(user)
                .from(user)
                .where(Optional.ofNullable(departmentId)
                        .map(user.department.id::eq)
                        .orElse(null))
                .fetch();
        return UserMapper.INSTANCE.fullList(res);
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

    public List<UserDTO> getAllUsersList(){
        QUser user = QUser.user;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        var res = queryBuilder.select(user)
                .from(user)
                .fetch();

        return UserMapper.INSTANCE.simpleList(res);
    }
}
