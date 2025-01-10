package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.UserEntity;

public interface UserService {

    UserDTO getUserDTOById(Long id);

    void createUser(NewUserDTO user);

    void updateUser(NewUserDTO updatedUser, Long id);

    void deleteUser(Long id);

    UserEntity getUserById(Long id);

    UserEntity saveUser(UserEntity newUser);
}
