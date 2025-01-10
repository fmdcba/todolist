package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUser(Long id) {
        return null;
    }

    @Override
    public void createUser(NewUserDTO user) {

    }

    @Override
    public void updateUser(NewUserDTO user) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}
