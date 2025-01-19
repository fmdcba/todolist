package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.*;
import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public interface UserService extends GenericService<UserEntity> {

    UserDTO getUser(String email, Long id) throws NotFoundException, InvalidArgumentException, UnauthorizedAccessException;

    List<UserDTO> getAllUsers(String authUserEmail) throws NotFoundException, UnauthorizedAccessException;

    void createUser(NewUserDTO user, String role) throws InvalidArgumentException, AlreadyExistsException, NotFoundException, UnauthorizedAccessException;

    void updateUser(NewUserDTO updatedUser, String email, Long id) throws NotFoundException, AlreadyExistsException, InvalidArgumentException, UnauthorizedAccessException;

    void deleteUser(String authUserEmail, Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedAccessException;
}
