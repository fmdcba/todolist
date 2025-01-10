package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.UserEntity;

public interface UserService {

    UserDTO getUserDTOById(Long id) throws NotFoundException, InvalidArgumentException;

    void createUser(NewUserDTO user) throws InvalidArgumentException, AlreadyExistsException;

    void updateUser(NewUserDTO updatedUser, Long id) throws NotFoundException, AlreadyExistsException, InvalidArgumentException;

    void deleteUser(Long id) throws InvalidArgumentException, NotFoundException;

    UserEntity getUserById(Long id) throws NotFoundException;

    UserEntity saveUser(UserEntity newUser);
}
