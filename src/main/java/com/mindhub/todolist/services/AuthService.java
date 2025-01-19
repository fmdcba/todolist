package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;

public interface AuthService {

    void registerUser(NewUserDTO newUser) throws AlreadyExistsException;

    String loginUser(NewUserDTO loginRequest) throws InvalidArgumentException, NotFoundException;
}
