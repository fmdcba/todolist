package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface UserService {

    UserDTO getUser(Long id, String auth, String email) throws NotFoundException, InvalidArgumentException, UnauthorizedException;

    void createUser(NewUserDTO user, String role) throws InvalidArgumentException, AlreadyExistsException;

    void createAdmin (NewUserDTO user) throws InvalidArgumentException, AlreadyExistsException;

    void updateUser(NewUserDTO updatedUser, Long id) throws NotFoundException, AlreadyExistsException, InvalidArgumentException, UnauthorizedException;

    void deleteUser(Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedException;

    UserEntity getUserById(Long id) throws NotFoundException;

    UserEntity saveUser(UserEntity newUser);

    Set<UserRecordDTO> getAllUsers(String role) throws UnauthorizedException;

    void registerUser(NewUserDTO newUser) throws AlreadyExistsException;

    void checkIfUserHasPermission(Long userId) throws UnauthorizedException, NotFoundException;

    void checkIfUserExists(NewUserDTO newUser) throws AlreadyExistsException;

    void checkIfUserExistsById(Long id) throws NotFoundException;

    void isUserId(Long id, String email) throws NotFoundException, UnauthorizedException;

    void isAdmin(String role) throws UnauthorizedException;
}
