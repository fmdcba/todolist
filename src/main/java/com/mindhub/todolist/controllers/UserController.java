package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.*;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.ControllerValidations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerValidations controllerValidations;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "If logged as user, return only if it is its own id. If logged as Admin returns any user")
        @ApiResponse(responseCode = "200", description = "Return user data, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
        @ApiResponse(responseCode = "401", description = "Error msg Unauthorized trying to get other users ID with user role")
    public UserDTO getUser(@PathVariable long id) throws NotFoundException, InvalidArgumentException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        return userService.getUser(authUserEmail, id);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns all users if logged as admin")
    @ApiResponse(responseCode = "200", description = "Returns a collection of all users")
    @ApiResponse(responseCode = "401", description = "Error msg Unauthorized when trying to get all users with user role")
    public ResponseEntity<?> getAllUsers() throws NotFoundException, UnauthorizedAccessException {
        String authUserEmail = controllerValidations.getAuthUserEmail();

        List<UserDTO> users = userService.getAllUsers(authUserEmail);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Recieves a user, posts it and return a confirmation message")
        @ApiResponse(responseCode = "201", description = "confirmation msg on body: User created")
        @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO, Authentication authentication) throws AlreadyExistsException, InvalidArgumentException, NotFoundException, UnauthorizedAccessException {
        controllerValidations.validateEntries(newUserDTO);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        userService.createUser(newUserDTO, authUserEmail);
        return new ResponseEntity<>("User created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit user", description = "Edit a user or any of it's field")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User updated")
        @ApiResponse(responseCode = "404", description = "When trying to patch non existent user. Confirmation msg on body: user not found")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser, @PathVariable Long id) throws NotFoundException, InvalidArgumentException, AlreadyExistsException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        validateEntriesUpdate(updatedUser);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        userService.updateUser(updatedUser, authUserEmail, id);
        return new ResponseEntity<>("User updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User deleted")
        @ApiResponse(responseCode = "400", description = "When trying to delete a user with invalid ID. Confimations msg on body: invalid ID")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        userService.deleteUser(authUserEmail, id);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }

    public void validateEntriesUpdate(NewUserDTO newUserDTO) throws InvalidArgumentException {
        controllerValidations.validateUsername(newUserDTO.username());
        controllerValidations.validatePassword(newUserDTO.password());
    }
}
