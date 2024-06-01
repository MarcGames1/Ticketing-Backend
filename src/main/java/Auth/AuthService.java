package Auth;

import Auth.DTO.LoginDTO;
import Auth.DTO.SignupDTO;
import Entities.Department;
import Entities.QUser;
import Entities.User;
import Enums.EmployeeRole;
import Mapper.UserMapper;
import Shared.DTO.AuthResponse;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
@Transactional
public class AuthService {
    @PersistenceContext
    private EntityManager entityManager;

    public AuthService(){

    }

    public AuthResponse signup(SignupDTO dto) {
        var existedUser = getUserByEmail(dto.getEmail());
        if(existedUser != null)
            throw new NotAllowedException("This email is already existed");
        User user = UserMapper.INSTANCE.signup(dto);
        if(dto.getDepartmentId() != null){
            Department department = entityManager.find(Department.class, dto.getDepartmentId());
            if(department == null)
                throw new NotFoundException("Department not found");
            user.setDepartment(department);
        }
        user.setRole(EmployeeRole.EMPLOYEE);
        entityManager.persist(user);
        return UserMapper.INSTANCE.auth(user);
    }

    private User getUserByEmail(String email){
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(user)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    public AuthResponse login(LoginDTO dto){
        var model = getUserByEmail(dto.getEmail());

        if(model == null)
            throw new NotFoundException("Email or password is incorrect");

        //TODO:Hashed passwords comparison
        if(!model.isCorrectPassword(dto.getPassword())){
            throw new NotFoundException("Email or password is incorrect");
        }

        return UserMapper.INSTANCE.auth(model);
    }
}
