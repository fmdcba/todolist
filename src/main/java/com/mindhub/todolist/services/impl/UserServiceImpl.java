package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.TaskRecordDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.dtos.UserRecordDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserDTOById(Long id) throws NotFoundException, UnauthorizedException {
        checkIfUserHasPermission(id);

        return new UserDTO(getUserById(id));
    }

    @Override
    public Set<UserRecordDTO> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        Set<UserRecordDTO>  userRecords = users
                .stream()
                .map(userEntity -> new UserRecordDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getRole())).collect(Collectors.toSet());
        return userRecords;
    }

    @Override
    public void createUser(NewUserDTO newUser) throws AlreadyExistsException {
        checkIfUserExists(newUser);
        UserEntity user = new UserEntity(newUser.username(), passwordEncoder.encode(newUser.password()), newUser.email(), RoleType.USER);

        saveUser(user);
    }

    @Override
    public void createAdmin(NewUserDTO newUser) throws AlreadyExistsException {
        checkIfUserExists(newUser);
        UserEntity user = new UserEntity(newUser.username(), passwordEncoder.encode(newUser.password()), newUser.email(), RoleType.ADMIN);

        saveUser(user);
    }

    @Override
    public void updateUser(NewUserDTO updatedUser, Long id) throws NotFoundException, AlreadyExistsException, UnauthorizedException {
        checkIfUserExists(updatedUser);
        checkIfUserHasPermission(id);

        UserEntity user = getUserById(id);

        saveUser(user);
    }

    @Override
    public void deleteUser(Long id) throws NotFoundException, UnauthorizedException {
        checkIfUserExistsById(id);
        checkIfUserHasPermission(id);

        userRepository.deleteById(id);
    }

    // Validations

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

    public void checkIfUserHasPermission(Long userId) throws UnauthorizedException, NotFoundException  {
        UserEntity user = getUserById(userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        UserEntity currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().toString().equals("ADMIN");

        if (!user.getId().equals(currentUser.getId()) && !isAdmin) {
            throw new UnauthorizedException("Unauthorized");
        }
    }
}
