package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;

public interface UserService {

    UserDTO getUser(Long id);

    void createUser(NewUserDTO user);

    void updateUser(NewUserDTO user);

    void deleteUser(Long id);
}
