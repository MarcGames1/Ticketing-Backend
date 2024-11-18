package Auth;

import Auth.DTO.*;
import Entities.Department;
import Entities.QUser;
import Entities.User;
import Enums.EmployeeRole;
import Mapper.UserMapper;
import Shared.DTO.AuthResponse;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Stateless
@Transactional
public class AuthService {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private CognitoService cognitoService;

    private static final String PASSWORD_POLICY_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public AuthService(){

    }

    public SignupResponse signup(SignupDTO dto) {
        if (!Pattern.matches(PASSWORD_POLICY_REGEX, dto.getPassword())) {
            throw new NotAllowedException("Password does not conform with policy","",new String[]{});
        }
        var existedUser = getUserByEmail(dto.getEmail());
        var emailExist = cognitoService.isEmailExists(dto.getEmail());
        if(existedUser != null && emailExist)
            throw new NotAllowedException("This email is already existed","",new String[]{});

        if(!emailExist){
            try{
                cognitoService.signUp(dto.getEmail(), dto.getPassword());
            } catch (Exception e){
                throw new ClientErrorException("Something went wrong", Response.Status.fromStatusCode(400));
            }
        }

        if(existedUser == null){
            User user = UserMapper.INSTANCE.signup(dto);
            if(dto.getDepartmentId() != null){
                Department department = entityManager.find(Department.class, dto.getDepartmentId());
                if(department == null)
                    throw new NotFoundException("Department not found");
                user.setDepartment(department);
            }
            user.setRole(EmployeeRole.EMPLOYEE);

            entityManager.persist(user);
        }

        var res = new SignupResponse();
        if(cognitoService.isEmailVerified(dto.getEmail())){
            res.nextAction = RequiredFlowAction.Login;
        } else {
            res.nextAction = RequiredFlowAction.VerifyEmail;
        }

        return res;
    }

    public User getUserByEmail(String email){
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(user)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    public AuthResponse login(LoginDTO dto){
        var model = getUserByEmail(dto.getEmail());
        var emailExist = cognitoService.isEmailExists(dto.getEmail());

        if(!emailExist){
            throw new NotFoundException("Email not found, you should Signup first");
        }

        if(!cognitoService.isEmailVerified(dto.getEmail())){
            throw new NotAllowedException("You should verify your email first","",new String[]{});
        }

        if(model == null)
            throw new NotFoundException("Your email is registered but no data found, you need to Signup with this email to fill the required data");

        Map<String, String> tokens = new HashMap<>();
        try{
            tokens.putAll(cognitoService.login(dto.getEmail(), dto.getPassword()));
        } catch (Exception e){
            throw new NotFoundException("Email or password is incorrect");
        }

        var res = UserMapper.INSTANCE.auth(model);
        res.refreshToken = tokens.get("refreshToken");
        res.idToken = tokens.get("idToken");
        res.accessToken = tokens.get("accessToken");
        return res;
    }

    public RefreshTokenResponse refreshToken(RefreshTokenDTO dto){
        var tokens = cognitoService.refreshTokens(dto.accessToken, dto.refreshToken);
        var res = new RefreshTokenResponse(){};
        res.idToken = tokens.get("idToken");
        res.accessToken = tokens.get("accessToken");
        return res;
    }
}
