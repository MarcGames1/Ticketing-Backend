package Mapper;

import Department.DTO.CreateDepartmentDTO;
import Department.DTO.UpdateDepartmentDTO;
import Entitys.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper( DepartmentMapper.class );

    Department create(CreateDepartmentDTO dto);
    Department update(@MappingTarget Department department, UpdateDepartmentDTO dto);
}