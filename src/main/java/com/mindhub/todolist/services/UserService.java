package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.TaskRecordDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.models.UserEntity;

import java.util.Set;

public interface UserService {

    UserDTO getUserDTOById(Long id) throws NotFoundException, InvalidArgumentException, UnauthorizedException;

    void createUser(NewUserDTO user) throws InvalidArgumentException, AlreadyExistsException;

    void createAdmin (NewUserDTO user) throws InvalidArgumentException, AlreadyExistsException;

    void updateUser(NewUserDTO updatedUser, Long id) throws NotFoundException, AlreadyExistsException, InvalidArgumentException, UnauthorizedException;

    void deleteUser(Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedException;

    UserEntity getUserById(Long id) throws NotFoundException;

    UserEntity saveUser(UserEntity newUser);

    Set<UserRecordDTO> getAllUsers();

    void checkIfUserHasPermission(Long userId) throws UnauthorizedException, NotFoundException;

    void checkIfUserExists(NewUserDTO newUser) throws AlreadyExistsException;

    void checkIfUserExistsById(Long id) throws NotFoundException;
}
