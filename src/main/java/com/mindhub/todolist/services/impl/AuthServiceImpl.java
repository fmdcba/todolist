package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.mappers.UserMapper;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.ServiceValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    ServiceValidations serviceValidations;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public void registerUser(NewUserDTO newUser) throws AlreadyExistsException {
        serviceValidations.validateAlreadyExistsEntries(newUser.username(), newUser.email());
        UserEntity user = userMapper.registerUserToEntity(newUser);

        userService.save(user);
    }


    public String loginUser(NewUserDTO loginRequest) throws NotFoundException {
        serviceValidations.validateExistsEmail(loginRequest.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateToken(authentication.getName());
    }
}
