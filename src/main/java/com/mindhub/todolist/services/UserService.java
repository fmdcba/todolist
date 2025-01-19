package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.*;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface UserService extends GenericService<UserEntity> {

    UserDTO getUser(String email, Long id) throws NotFoundException, InvalidArgumentException, UnauthorizedAccessException;

    void createUser(NewUserDTO user, String role) throws InvalidArgumentException, AlreadyExistsException, NotFoundException, UnauthorizedAccessException;

    void updateUser(NewUserDTO updatedUser, String email, Long id) throws NotFoundException, AlreadyExistsException, InvalidArgumentException, UnauthorizedAccessException;

    void deleteUser(String authUserEmail, Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedAccessException;

    UserEntity getUserById(Long id) throws NotFoundException;

    UserEntity saveUser(UserEntity newUser);

    List<UserDTO> getAllUsers(String role) throws NotFoundException, UnauthorizedAccessException;

    void registerUser(NewUserDTO newUser) throws AlreadyExistsException;

    void checkIfUserExists(NewUserDTO newUser) throws AlreadyExistsException;

    void validateAlreadyExistsEmail(String email) throws AlreadyExistsException;

    void validateIsAuthorized(UserEntity authUser, Long id) throws UnauthorizedAccessException;
}
