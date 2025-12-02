package org.example.ecommerceorderandinventory.mappers;

import org.example.ecommerceorderandinventory.dto.in.CreateUserDTO;
import org.example.ecommerceorderandinventory.dto.out.UserDTO;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromDTOToUser(CreateUserDTO createUserDTO);

    List<User> fromDTOToUserList(List<CreateUserDTO> createUserDTO);

    List<UserDTO> fromUserToDTOList(List<User> createUserDTO);

    UserDTO fromUserToDTO(User user);
}
