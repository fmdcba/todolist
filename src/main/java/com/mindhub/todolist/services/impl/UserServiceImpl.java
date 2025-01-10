package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserDTOById(Long id) {
        return new UserDTO(getUserById(id));
    }

    @Override
    public void createUser(NewUserDTO newUser) {
        UserEntity user = new UserEntity(newUser.username(), newUser.password(), newUser.email());
        saveUser(user);
    }

    @Override
    public void updateUser(NewUserDTO updatedUser, Long id) {
        UserEntity user = getUserById(id);

        if (updatedUser.username() != null && !updatedUser.username().isBlank()) {
            user.setUsername(updatedUser.username());
        }
        if (updatedUser.email() != null && !updatedUser.email().isBlank()) {
            user.setEmail(updatedUser.email());
        }
        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            user.setPassword(updatedUser.password());
        }

        saveUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
