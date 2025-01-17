package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.TaskRecordDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.TaskService;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{id}")
    @Operation(summary = "Get user", description = "Return one user in particular and it's attributes")
    @ApiResponse(responseCode = "200", description = "Return the user with a status code of OK")
    @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
    public ResponseEntity<?> getUser(@PathVariable long id) throws NotFoundException, InvalidArgumentException {
        validateId(id);
        UserEntity user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @GetMapping("/user/all")
//    @Operation(summary = "Get all users", description = "Returns all users and their attributes")
//    @ApiResponse(responseCode = "200", description = "Returns a collection of all users")
//    public ResponseEntity<Set<UserRecordDTO>> getAllUsers() {
//        Set<UserRecordDTO> users = userService.getAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    @GetMapping("/task/all")
    @Operation(summary = "Get all tasks", description = "Returns all tasks and their attributes")
    @ApiResponse(responseCode = "200", description = "Returns a collection of all tasks")
    public ResponseEntity<Set<TaskRecordDTO>> getAllTasks() {
        Set<TaskRecordDTO> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create admin", description = "Recieves an admin, posts it and return a confirmation message")
    @ApiResponse(responseCode = "201", description = "confirmation msg on body: Admin created")
    @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> createAdmin(@RequestBody NewUserDTO newUserDTO) throws AlreadyExistsException, InvalidArgumentException {
        validateAdmin(newUserDTO);
        userService.createAdmin(newUserDTO);
        return new ResponseEntity<>("Admin created", HttpStatus.CREATED);
    }

    // Validations

    public void validateId(Long id) throws InvalidArgumentException {
        if (id == null  || id <= 0) {
            throw new InvalidArgumentException("Invalid ID");
        }
    }

    public void validateAdmin(NewUserDTO newUser) throws InvalidArgumentException {
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
