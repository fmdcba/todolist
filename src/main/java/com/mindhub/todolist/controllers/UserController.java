package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return userService.getUserDTOById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO) {
        userService.createUser(newUserDTO);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser,@PathVariable Long id) {
        userService.updateUser(updatedUser, id);
        return new ResponseEntity<>("updated user", HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("deleted user", HttpStatus.OK);
    }
}
