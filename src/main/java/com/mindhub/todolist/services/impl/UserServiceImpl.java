package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.Mappers.UserMapper;
import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.*;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.ServiceValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServiceValidations serviceValidations;

    @Override
    public UserDTO getUser(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);

        return userMapper.userToDTO(findById(id));
    }

    @Override
    public List<UserDTO> getAllUsers(String authUserEmail) throws NotFoundException,  UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateIsAdmin(authUser);

        return userMapper.usersListToDTO(findAll());
    }

    @Override
    public void createUser(NewUserDTO newUser, String authUserEmail) throws AlreadyExistsException, NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateAlreadyExistsEntries(newUser.username(), newUser.email());
        serviceValidations.validateIsAdmin(authUser);

        save(userMapper.userToEntity(newUser));
    }

    @Override
    public void updateUser(NewUserDTO updatedUser, String authUserEmail, Long id) throws NotFoundException, AlreadyExistsException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);
        serviceValidations.validateAlreadyExistsEntries(updatedUser.username(), updatedUser.email());

        UserEntity user = findById(id);

        save(userMapper.updateUserToEntity(updatedUser, user));
    }

    @Override
    public void deleteUser(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);

        deleteById(id);
    }

    @Override
    public UserEntity findById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
       userRepository.deleteById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}

