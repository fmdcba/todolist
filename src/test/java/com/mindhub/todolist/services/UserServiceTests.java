package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.mappers.UserMapper;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.impl.UserServiceImpl;
import com.mindhub.todolist.utils.Constants;
import com.mindhub.todolist.utils.Factory;
import com.mindhub.todolist.utils.ServiceValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private String authUserEmail;
    private Long validUserId;
    private Long NonExistentId;
    private UserEntity authUser;
    private UserEntity targetUser;
    private UserDTO expectedUserDTO;

    @BeforeEach
    public void setUp() {
        authUserEmail = Constants.VALID_EMAIL;
        validUserId = Constants.VALID_ID;
        NonExistentId = Constants.INVALID_ID;
        authUser = Factory.createValidUserEntity();
        targetUser = Factory.createValidTargetUserEntity();
        expectedUserDTO  = new UserDTO(targetUser);
    }

    @Test
    @DisplayName("When given valid ID returns a userDTO")
    public void getUser_whenGivenIdIsValid() throws NotFoundException, UnauthorizedAccessException {
        when(serviceValidations.validateExistsAuthenticatedUser(authUserEmail)).thenReturn(authUser);
        doNothing().when(serviceValidations).validateExistsId(validUserId);
        doNothing().when(serviceValidations).validateIsAuthorized(authUser, validUserId);
        when(userRepository.findById(validUserId)).thenReturn(Optional.of(targetUser));
        when(userMapper.userToDTO(targetUser)).thenReturn(expectedUserDTO);

        UserDTO result = userService.getUser(authUserEmail, validUserId);

        assertNotNull(result);
        assertEquals(expectedUserDTO.getId(), result.getId());
        assertEquals(expectedUserDTO.getUsername(), result.getUsername());
        assertEquals(expectedUserDTO.getEmail(), result.getEmail());
        assertEquals(expectedUserDTO.getTasks(), result.getTasks());
    }

    @Test
    @DisplayName("When given NonExistent ID throws NotFoundException")
    public void getUser_whenIdNonExistent_shouldThrowNotFound() throws NotFoundException {
        when(serviceValidations.validateExistsAuthenticatedUser(authUserEmail)).thenReturn(authUser);
        doThrow(new NotFoundException("Id '" + NonExistentId + "' Not found"))
                .when(serviceValidations).validateExistsId(NonExistentId);

        assertThrows(NotFoundException.class, () ->
                userService.getUser(authUserEmail, NonExistentId)
        );
    }

    @Test
    @DisplayName("When user is not authorized to access resource throws UnauthorizedAccessException")
    public void getUser_whenUserNotAuthorized_shouldThrowUnauthorizedAccessException() throws NotFoundException, UnauthorizedAccessException {
        when(serviceValidations.validateExistsAuthenticatedUser(authUserEmail)).thenReturn(authUser);
        doNothing().when(serviceValidations).validateExistsId(validUserId);
        doThrow(new UnauthorizedAccessException("User unable to access this resource."))
                .when(serviceValidations).validateIsAuthorized(authUser, validUserId);

        assertThrows(UnauthorizedAccessException.class, () ->
                userService.getUser(authUserEmail, validUserId)
        );
    }
}