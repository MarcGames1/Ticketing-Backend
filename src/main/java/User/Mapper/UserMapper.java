package User.Mapper;

import Entitys.User;
import Enums.EmployeeRole;
import User.DTO.CreateUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(source = "identifier", target = "email")
    User createDtoToModel(CreateUserDTO user);
}