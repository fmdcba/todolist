package com.mindhub.todolist.utils;

import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceValidations {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    public UserEntity validateExistsAuthenticatedUser(String authUserEmail) throws NotFoundException {
        return userRepository.findByEmail(authUserEmail).orElseThrow(() -> new NotFoundException("Authenticated user not found."));
    }


    public void validateExistsId(Long id) throws NotFoundException {
        if(!userRepository.existsById(id)){
            throw new NotFoundException("Id '" + id + "' Not found");
        }
    }

    public boolean validateIsUserId(UserEntity authUser, Long id) {
        return authUser.getId().equals(id);
    }

    public void validateIsAdmin(UserEntity authUser) throws UnauthorizedAccessException {
        String  userRole = authUser.getRole().toString();

        if (!userRole.equals("ADMIN")) {
            throw new  UnauthorizedAccessException("User unable to access this resource.");
        }
    }

    public void validateIsAuthorized(UserEntity authUser, Long id) throws UnauthorizedAccessException {
        if (!validateIsUserId(authUser, id)) {
            validateIsAdmin(authUser);
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

    public void validateAlreadyExistsEntries(String username, String email) throws AlreadyExistsException {
        validateAlreadyExistsUsername(username);
        validateAlreadyExistsEmail(email);
    }

    public void validateExistsEmail(String email) throws NotFoundException {
        if (!userRepository.existsByEmail(email)){
            throw new NotFoundException("Invalid Credentials.");
        }
    }
}
