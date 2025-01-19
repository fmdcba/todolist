package com.mindhub.todolist.utils;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ControllerValidations {

    public String getAuthUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void validateId(Long id) throws InvalidArgumentException {
        if (id == null || id <= 0) {
            throw new InvalidArgumentException("Invalid ID");
        }
    }

    public void validateUsername(String username) throws InvalidArgumentException {
        if (username == null || username.isBlank()) {
            throw new InvalidArgumentException("Username must not be null or empty");
        }
    }

    public void validateEmail(String email) throws InvalidArgumentException {
        if (email == null || email.isBlank()) {
            throw new InvalidArgumentException("Email must not be null or empty");
        }
    }

    public void validatePassword(String password) throws InvalidArgumentException {
        if (password == null || password.isBlank()) {
            throw new InvalidArgumentException("Password must not be null or empty");
        }
    }

    public void validateEntries(NewUserDTO newUserDTO) throws InvalidArgumentException {
        validateUsername(newUserDTO.username());
        validateEmail(newUserDTO.email());
        validatePassword(newUserDTO.password());
    }
}
