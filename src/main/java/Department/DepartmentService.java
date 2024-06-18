package Department;

import Department.DTO.BasicDepartmentDTO;
import Department.DTO.CreateDepartmentDTO;
import Department.DTO.DepartmentDTO;
import Department.DTO.UpdateDepartmentDTO;
import Entities.Department;
import Entities.QDepartment;
import Entities.QUser;
import Mapper.DepartmentMapper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;


@ApplicationScoped
@Transactional
public class DepartmentService {
    @PersistenceContext
    private EntityManager entityManager;

    public DepartmentService(){

    }

    public Long create(CreateDepartmentDTO dto) {
        Department model = DepartmentMapper.INSTANCE.create(dto);
        entityManager.persist(model);
        return model.getId();
    }

    public DepartmentDTO getById(Long id) {
        QDepartment department = QDepartment.department;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(department)
                .from(department)
                .where(department.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Department not found");
        return DepartmentMapper.INSTANCE.getOne(model);
    }

    public List<DepartmentDTO> getAll(){
        QDepartment department = QDepartment.department;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var res = query.select(department)
                .from(department)
                .fetch();
        return DepartmentMapper.INSTANCE.getAll(res);
    }

    public List<BasicDepartmentDTO> getList(){
        QDepartment department = QDepartment.department;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var res = query.select(department)
                .from(department)
                .fetch();
        return DepartmentMapper.INSTANCE.getList(res);
    }

    public void update(UpdateDepartmentDTO dto) {
        var model = entityManager.find(Department.class, dto.getId());
        if(model == null)
            throw new NotFoundException("Department not found");
        model = DepartmentMapper.INSTANCE.update(model, dto);
        entityManager.merge(model);
    }

    public Long delete(Long id) {
        QUser user = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var existedUsers = query.select(user)
                .from(user)
                .where(user.department.id.eq(id))
                .fetch();
        if(existedUsers != null && !existedUsers.isEmpty())
            throw new NotAllowedException("This department has users linked to it","",new String[]{});
        QDepartment department = QDepartment.department;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        return queryBuilder.delete(department)
                .where(department.id.eq(id))
                .execute();
    }
}
