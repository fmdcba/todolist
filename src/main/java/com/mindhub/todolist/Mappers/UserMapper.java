package com.mindhub.todolist.Mappers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity userToEntity (NewUserDTO user) {
        UserEntity userEntity = new UserEntity(user.username(), passwordEncoder.encode(user.password()), user.email(), (RoleType) user.role());

        return userEntity;
    }

    public UserEntity updateUserToEntity(NewUserDTO updatedUser, UserEntity user) {
        user.setUsername(updatedUser.username());
        user.setPassword(passwordEncoder.encode(updatedUser.password()));

        return user;
    }

    public UserEntity registerUserToEntity (NewUserDTO user) {
        UserEntity userEntity = new UserEntity(user.username(), passwordEncoder.encode(user.password()), user.email(), RoleType.USER);

        return userEntity;
    }

    public UserDTO userToDTO (UserEntity user) {
        return new UserDTO(user);
    }

    public List<UserDTO> usersListToDTO (List<UserEntity> usersList) {
        return usersList.stream().map(user -> new UserDTO(user)).toList();
    }
}
