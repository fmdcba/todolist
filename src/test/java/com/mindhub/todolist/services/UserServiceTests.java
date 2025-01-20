package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.AlreadyExistsException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.mappers.UserMapper;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.impl.UserServiceImpl;
import com.mindhub.todolist.utils.ServiceValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceValidations serviceValidations;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private NewUserDTO newUserDTO;
    private String authUserEmail;
    private UserEntity authUser;
    private UserEntity mappedUserEntity;

    @BeforeEach
    public void setUp() {
        newUserDTO = new NewUserDTO(
                null,
                "testUser",
                "password",
                "test@example.com",
                RoleType.USER
        );

        authUserEmail = "admin@example.com";
        authUser = new UserEntity("admin", "encodedPass", authUserEmail, RoleType.ADMIN);
        mappedUserEntity = new UserEntity("testUser", "encodedPassword", "test@example.com", RoleType.USER);
    }

    @Test
    public void createUser_WhenPassedValidDTO_ShouldCallDependenciesAndSave()
            throws AlreadyExistsException, NotFoundException, UnauthorizedAccessException {
        when(serviceValidations.validateExistsAuthenticatedUser(authUserEmail)).thenReturn(authUser);
        doNothing().when(serviceValidations).validateAlreadyExistsEntries(newUserDTO.username(), newUserDTO.email());
        doNothing().when(serviceValidations).validateIsAdmin(authUser);
        when(userMapper.userToEntity(newUserDTO)).thenReturn(mappedUserEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mappedUserEntity);

        userService.createUser(newUserDTO, authUserEmail);

        verify(serviceValidations).validateExistsAuthenticatedUser(authUserEmail);
        verify(serviceValidations).validateAlreadyExistsEntries(newUserDTO.username(), newUserDTO.email());
        verify(serviceValidations).validateIsAdmin(authUser);
        verify(userMapper).userToEntity(newUserDTO);
        verify(userRepository).save(mappedUserEntity);
    }

    @Test
    public void throwException_WhenUsernameAlreadyExits()
            throws AlreadyExistsException, NotFoundException, UnauthorizedAccessException {
        when(serviceValidations.validateExistsAuthenticatedUser(authUserEmail)).thenReturn(authUser);
        doNothing().when(serviceValidations).validateAlreadyExistsEntries(newUserDTO.username(), newUserDTO.email());
        doNothing().when(serviceValidations).validateIsAdmin(authUser);
        when(userMapper.userToEntity(newUserDTO)).thenReturn(mappedUserEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mappedUserEntity);

        userService.createUser(newUserDTO, authUserEmail);

        verify(serviceValidations).validateExistsAuthenticatedUser(authUserEmail);
        verify(serviceValidations).validateAlreadyExistsEntries(newUserDTO.username(), newUserDTO.email());
        verify(serviceValidations).validateIsAdmin(authUser);
        verify(userMapper).userToEntity(newUserDTO);
        verify(userRepository).save(mappedUserEntity);
    }
}