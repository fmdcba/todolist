package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "If logged as user, return only if it is its own id. If logged as Admin returns any user")
        @ApiResponse(responseCode = "200", description = "Return user data, and http code status OK")
        @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
        @ApiResponse(responseCode = "401", description = "Error msg Unauthorized trying to get other users ID with user role")
    public UserDTO getUser(@PathVariable long id, Authentication authentication) throws NotFoundException, InvalidArgumentException, UnauthorizedException {
        validateId(id);
        String userRole = getUserRole(authentication);
        String userEmail =  getUserEmail(authentication);

        return userService.getUser(id, userRole, userEmail);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns all users if logged as admin")
    @ApiResponse(responseCode = "200", description = "Returns a collection of all users")
    @ApiResponse(responseCode = "401", description = "Error msg Unauthorized when trying to get all users with user role")
    public ResponseEntity<Set<UserRecordDTO>> getAllUsers(Authentication authentication) throws UnauthorizedException {
        String userRole = getUserRole(authentication);

        Set<UserRecordDTO> users = userService.getAllUsers(userRole);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Recieves a user, posts it and return a confirmation message")
        @ApiResponse(responseCode = "201", description = "confirmation msg on body: User created")
        @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO, Authentication authentication) throws AlreadyExistsException, InvalidArgumentException {
        validateUser(newUserDTO);
        String userRole = getUserRole(authentication);

        userService.createUser(newUserDTO, userRole);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit user", description = "Edit a user or any of it's field")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User updated")
        @ApiResponse(responseCode = "404", description = "When trying to patch non existent user. Confirmation msg on body: user not found")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser,@PathVariable Long id) throws NotFoundException, InvalidArgumentException, AlreadyExistsException, UnauthorizedException {
        validateId(id);
        userService.updateUser(updatedUser, id);
        return new ResponseEntity<>("updated user", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: User deleted")
        @ApiResponse(responseCode = "400", description = "When trying to delete a user with invalid ID. Confimations msg on body: invalid ID")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedException {
        validateId(id);
        userService.deleteUser(id);
        return new ResponseEntity<>("deleted user", HttpStatus.OK);
    }

    @GetMapping("/test")
    public String getUserName(Authentication authentication){
        Collection<?> auth = authentication.getAuthorities();
        // String cred = authentication.getCredentials();
        String det = authentication.getDetails().toString();
        String name = authentication.getName();

        return authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList()
                .toString();
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

    public String getUserRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList()
                .toString();
    }

    public String getUserEmail(Authentication authentication) {
        return authentication.getName();
    }
}
