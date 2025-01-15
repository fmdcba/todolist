package com.mindhub.todolist.controllers;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.LoginUserDTO;
import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Login a user and return a JTW in plain text")
    @ApiResponse(responseCode = "200", description = "Confirmation msg on body: User created")
    @ApiResponse(responseCode = "403", description = "Invalid Credentials")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginUserDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(jwt);
    }


    @PostMapping("/register")
    @Operation(summary = "Sign in", description = "Receives credential for a user in the body and return a confirmation message")
    @ApiResponse(responseCode = "201", description = "confirmation msg on body: User created")
    @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO newUserDTO) throws AlreadyExistsException, InvalidArgumentException {
        validateUser(newUserDTO);
        userService.createUser(newUserDTO);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
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
