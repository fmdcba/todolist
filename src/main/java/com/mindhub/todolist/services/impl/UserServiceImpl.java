package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.Mappers.UserMapper;
import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.*;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getUser(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = getAuthenticatedUser(authUserEmail);

        validateExistsId(id);
        validateIsAuthorized(authUser, id);

        return userMapper.userToDTO(findById(id));
    }

    @Override
    public List<UserDTO> getAllUsers(String authUserEmail) throws NotFoundException,  UnauthorizedAccessException {
        UserEntity authUser = getAuthenticatedUser(authUserEmail);

        validateIsAdmin(authUser);

        return userMapper.usersListToDTO(findAll());
    }

    @Override
    public void createUser(NewUserDTO newUser, String authUserEmail) throws AlreadyExistsException, NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = getAuthenticatedUser(authUserEmail);

        validateAlreadyExistsEmail(newUser.email());
        validateIsAdmin(authUser);

        save(userMapper.userToEntity(newUser));
    }

    @Override
    public void updateUser(NewUserDTO updatedUser, String authUserEmail, Long id) throws NotFoundException, AlreadyExistsException, UnauthorizedAccessException {
        UserEntity authUser = getAuthenticatedUser(authUserEmail);

        validateExistsId(id);
        validateIsAuthorized(authUser, id);
        if(!validateIsUserId(authUser, id)){
            validateAlreadyExistsEmail(updatedUser.email());
            validateAlreadyExistsUsername(updatedUser.username());
        }

        UserEntity user = getUserById(id);

        save(userMapper.updateToEntity(updatedUser, user));
    }

    @Override
    public void deleteUser(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = getAuthenticatedUser(authUserEmail);

        validateExistsId(id);
        validateIsAuthorized(authUser, id);

        deleteById(id);
    }

    //

    @Override
    public void registerUser(NewUserDTO newUser) throws AlreadyExistsException {
        checkIfUserExists(newUser);
        UserEntity user = new UserEntity(newUser.username(), passwordEncoder.encode(newUser.password()), newUser.email(), RoleType.USER);

        saveUser(user);
    }

    // Validations

    @Override
    public UserEntity getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public void checkIfUserExists(NewUserDTO newUser) throws AlreadyExistsException {
        if (userRepository.existsByUsername(newUser.username())) {
            throw new AlreadyExistsException("Username already in use");
        }

        if (userRepository.existsByEmail(newUser.email())) {
            throw new AlreadyExistsException("Email already in use");
        }
    }

    @Override
    public UserEntity findById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
       userRepository.deleteById(id);
    }

    public UserEntity getAuthenticatedUser(String authUserEmail) throws NotFoundException {
        return userRepository.findByEmail(authUserEmail).orElseThrow(() -> new NotFoundException("Not found"));
    }

    public void validateIsAdmin(UserEntity authUser) throws UnauthorizedAccessException {
        String  userRole = authUser.getRole().toString();

        if (!userRole.equals("ADMIN")) {
            throw new  UnauthorizedAccessException("User  access this resource.");
        }
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    public void validateExistsId(Long id) throws NotFoundException{
        if(!userRepository.existsById(id)){
            throw new NotFoundException("Id '" + id + "' Not found");
        }
    }

    public void validateAlreadyExistsEmail(String email) throws AlreadyExistsException {
        if (userRepository.existsByEmail(email)){
            throw new AlreadyExistsException("User email '" + email + "' is already taken");
        }
    }

    public void validateAlreadyExistsUsername(String username) throws AlreadyExistsException {
        if (userRepository.existsByUsername(username)){
            throw new AlreadyExistsException("Username '" + username + "' is already taken");
        }
    }

    public boolean validateIsUserId(UserEntity authUser, Long id) {
        return authUser.getId().equals(id);
    }

    public void validateIsAuthorized(UserEntity authUser, Long id) throws UnauthorizedAccessException {
        if (!validateIsUserId(authUser, id)) {
            validateIsAdmin(authUser);
        }
    }
}

