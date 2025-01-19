package com.mindhub.todolist.controllers;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.utils.ControllerValidations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    ControllerValidations controllerValidations;

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Login a user and return a JTW in plain text")
    @ApiResponse(responseCode = "200", description = "Confirmation msg on body: User created")
    @ApiResponse(responseCode = "403", description = "Invalid Credentials")
    public ResponseEntity<String> authenticateUser(@RequestBody NewUserDTO loginRequest) throws InvalidArgumentException, NotFoundException {
        validateEntriesLogin(loginRequest);
        String jwt = authService.loginUser(loginRequest);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    @Operation(summary = "Sign in", description = "Receives credential for a user in the body and return a confirmation message")
    @ApiResponse(responseCode = "201", description = "confirmation msg on body: User created")
    @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: User title must not be null or empty")
    public ResponseEntity<?> registerUser(@RequestBody NewUserDTO newUserDTO) throws AlreadyExistsException, InvalidArgumentException {
        controllerValidations.validateEntries(newUserDTO);
        authService.registerUser(newUserDTO);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    public void validateEntriesLogin(NewUserDTO newUser) throws InvalidArgumentException {
        controllerValidations.validateEmail(newUser.email());
        controllerValidations.validatePassword(newUser.password());
    }
}
