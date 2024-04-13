package Mapper;

import Entities.User;
import User.DTO.CreateUserDTO;
import User.DTO.UpdateUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(source = "identifier", target = "email")
    User create(CreateUserDTO user);

    User update(@MappingTarget User user, UpdateUserDTO dto);
}