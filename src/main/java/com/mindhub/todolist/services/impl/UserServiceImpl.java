package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.NotFoundException;
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
    public UserDTO getUserDTOById(Long id) throws NotFoundException {

        return new UserDTO(getUserById(id));
    }

    @Override
    public void createUser(NewUserDTO newUser) throws AlreadyExistsException {
        checkIfUserExists(newUser);
        UserEntity user = new UserEntity(newUser.username(), newUser.password(), newUser.email());

        saveUser(user);
    }

    @Override
    public void updateUser(NewUserDTO updatedUser, Long id) throws NotFoundException, AlreadyExistsException {
        UserEntity user = getUserById(id);
        checkIfUserExists(updatedUser);

        saveUser(user);
    }

    @Override
    public void deleteUser(Long id) throws NotFoundException {
        checkIfUserExistsById(id);

        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public void checkIfUserExists(NewUserDTO newUser) throws AlreadyExistsException {
        if (userRepository.existsByUsername(newUser.username())) {
            throw new AlreadyExistsException("Username already in use");
        }

        if (userRepository.existsByEmail(newUser.email())) {
            throw new AlreadyExistsException("Email already in use");
        }
    }

    public void checkIfUserExistsById(Long id) throws NotFoundException {
        if(!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exists");
        }
    }
}
