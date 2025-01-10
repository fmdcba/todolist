package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) throws NotFoundException, InvalidArgumentException {
        validateId(id);
        return userService.getUserDTOById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO) throws AlreadyExistsException, InvalidArgumentException {
        userService.createUser(newUserDTO);
        validateUser(newUserDTO);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser,@PathVariable Long id) throws NotFoundException, InvalidArgumentException, AlreadyExistsException {
        userService.updateUser(updatedUser, id);
        return new ResponseEntity<>("updated user", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws InvalidArgumentException, NotFoundException {
        validateId(id);
        userService.deleteUser(id);
        return new ResponseEntity<>("deleted user", HttpStatus.OK);
    }

    public void validateId(Long id) throws InvalidArgumentException {
        if (id == null  || id <= 0) {
            throw new InvalidArgumentException("Invalid ID");
        }
    }

    public void validateUser(NewUserDTO newUser) throws InvalidArgumentException {
        if (newUser.username() == null || newUser.username().isBlank()) {
            throw new InvalidArgumentException("Username must not be null or empty");
        }

        if (newUser.email() == null || newUser.email().isBlank()) {
            throw new InvalidArgumentException("Email must not be null or empty");
        }

        if (newUser.password() == null || newUser.password().isBlank()) {
            throw new InvalidArgumentException("Password must not be null or empty");
        }
    }
}
