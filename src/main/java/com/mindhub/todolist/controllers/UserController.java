package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO) {
        UserEntity newUser = new UserEntity(newUserDTO.username(), newUserDTO.password(), newUserDTO.email());
        userRepository.save(newUser);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser() {
        return new UserDTO(userRepository.findById(1L).orElse(null));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody NewUserDTO updatedUser,@PathVariable Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);

        if (updatedUser.username() != null && !updatedUser.username().isBlank()) {
            user.setUsername(updatedUser.username());
        }
        if (updatedUser.email() != null && !updatedUser.email().isBlank()) {
            user.setEmail(updatedUser.email());
        }
        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            user.setPassword(updatedUser.password());
        }


        userRepository.save(user);
        return new ResponseEntity<>("updated user", HttpStatus.OK);
    }
}
