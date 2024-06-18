package Mapper;

import Department.DTO.BasicDepartmentDTO;
import Department.DTO.CreateDepartmentDTO;
import Department.DTO.DepartmentDTO;
import Department.DTO.UpdateDepartmentDTO;
import Entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper( DepartmentMapper.class );

    Department create(CreateDepartmentDTO dto);
    Department update(@MappingTarget Department department, UpdateDepartmentDTO dto);
    List<DepartmentDTO> getAll(List<Department> departments);

    List<BasicDepartmentDTO> getList(List<Department> departments);

    @Mapping(source = "users", target = "usersCount")
    DepartmentDTO getOne(Department department);

    default int mapListToCount(List<?> list) {
        return list == null ? 0 : list.size();
    }
}