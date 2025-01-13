package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get user", description = "Return a user and it's attributes")
        @ApiResponse(responseCode = "200", description = "Return the user with a status code of OK")
        @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
    public UserDTO getUser(@PathVariable long id) throws NotFoundException, InvalidArgumentException {
        validateId(id);
        return userService.getUserDTOById(id);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Recieves a user, posts it and return a confirmation message")
        @ApiResponse(responseCode = "201", description = "confirmation msg on body: User created")
        @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO) throws AlreadyExistsException, InvalidArgumentException {
        validateUser(newUserDTO);
        userService.createUser(newUserDTO);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit user", description = "Edit a user or any of it's field")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User updated")
        @ApiResponse(responseCode = "404", description = "When trying to patch non existent user. Confirmation msg on body: user not found")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser,@PathVariable Long id) throws NotFoundException, InvalidArgumentException, AlreadyExistsException {
        validateId(id);
        userService.updateUser(updatedUser, id);
        return new ResponseEntity<>("updated user", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User deleted")
        @ApiResponse(responseCode = "400", description = "When trying to delete a user with invalid ID. Confimations msg on body: invalid ID")
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
