package Department;

import Department.DTO.CreateDepartmentDTO;
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

    public Department getById(Long id) {
//        What is QDepartment?
        QDepartment department = QDepartment.department;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        var model = query.select(department)
                .from(department)
                .where(department.id.eq(id))
                .fetchOne();
        if(model == null)
            throw  new NotFoundException("Department not found");
        return model;
    }

    public List<Department> getAll(){
        QDepartment department = QDepartment.department;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(department)
                .from(department)
                .fetch();
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
        var existedUser = query.select(user)
                .from(user)
                .where(user.department.id.eq(id))
                .fetchOne();
        if(existedUser != null)
            throw new NotAllowedException("This department has users linked to it");
        QDepartment department = QDepartment.department;
        JPAQueryFactory queryBuilder = new JPAQueryFactory(entityManager);
        return queryBuilder.delete(department)
                .where(department.id.eq(id))
                .execute();
    }
}
