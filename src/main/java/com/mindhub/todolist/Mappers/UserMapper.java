package com.mindhub.todolist.Mappers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity userToEntity (NewUserDTO user) {
        UserEntity userEntity = new UserEntity(user.username(), passwordEncoder.encode(user.password()), user.email(), RoleType.ADMIN);

        return userEntity;
    }

    public UserEntity updateToEntity(NewUserDTO updatedUser, UserEntity user) {
        user.setUsername(updatedUser.username());
        user.setPassword(passwordEncoder.encode(updatedUser.password()));

        return user;
    }

    public UserDTO userToDTO (UserEntity user) {
        return new UserDTO(user);
    }

    public List<UserDTO> usersListToDTO (List<UserEntity> usersList) {
        return usersList.stream().map(user -> new UserDTO(user)).toList();
    }
}
