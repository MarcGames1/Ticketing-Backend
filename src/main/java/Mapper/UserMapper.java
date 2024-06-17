package Mapper;

import Auth.DTO.SignupDTO;
import Entities.User;
import Shared.DTO.AuthResponse;
import Shared.DTO.UserDTO;
import User.DTO.CreateUserDTO;
import User.DTO.UpdateUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(source = "identifier", target = "email")
    User create(CreateUserDTO user);

    User update(@MappingTarget User user, UpdateUserDTO dto);

    User signup(SignupDTO dto);
    AuthResponse auth(User user);

    UserDTO simple(User user);
    List<UserDTO> simpleList(List<User> users);
}