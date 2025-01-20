package com.mindhub.todolist.repositories;

import com.mindhub.todolist.config.TestConfig;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.utils.Constants;
import com.mindhub.todolist.utils.Factory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("When given valid email returns a user entity")
    public void findUser_whenEmailIsValid() {
        UserEntity sut = Factory.createValidUserEntity();
        userRepository.save(sut);

        Optional<UserEntity> foundUser = userRepository.findByEmail(Constants.VALID_EMAIL);

        assertNotNull(foundUser);
        assertEquals(Constants.VALID_EMAIL, foundUser.get().getEmail());
    }

    @Test
    @DisplayName("When given non-existent email returns empty")
    public void notFindUser_whenEmailIsNotValid() {
        String nonExistentSutEmail = Constants.NON_EXISTENT_EMAIL;

        Optional<UserEntity> foundUser = userRepository.findByEmail(nonExistentSutEmail);

        assertNotNull(foundUser);
        assertTrue(foundUser.isEmpty(), "Expected no user to be found for a non-existent email");
    }

    @Test
    @DisplayName("when given valid username returns true")
    public void returnTrue_WhenExistsUserByUsername() {
        UserEntity sut = Factory.createValidUserEntity();
        userRepository.save(sut);

        Boolean foundUser = userRepository.existsByUsername(Constants.VALID_USERNAME);

        assertNotNull(foundUser);
        assertEquals(true, foundUser);
    }

    @Test
    @DisplayName("when given non-existent username returns false")
    public void returnFalse_whenNotExistsUserByUsername() {
        String nonExistentSutUsername = Constants.NON_EXISTENT_USERNAME;

        Boolean foundUser = userRepository.existsByUsername(nonExistentSutUsername);

        assertNotNull(foundUser);
        assertFalse(foundUser, "Repository should return false for a non-existent username");
    }

    @Test
    @DisplayName("when given valid email returns true")
    public void returnTrue_whenExistsUserByEmail() {
        UserEntity sut = Factory.createValidUserEntity();
        userRepository.save(sut);

        Boolean foundUser = userRepository.existsByEmail(Constants.VALID_EMAIL);

        assertNotNull(foundUser);
        assertEquals(true, foundUser);
    }

    @Test
    @DisplayName("when given non-existent email returns false")
    public void returnFalse_whenNotExistsUserByEmail() {
        String nonExistentEmail = Constants.NON_EXISTENT_EMAIL;

        Boolean foundUser = userRepository.existsByEmail(nonExistentEmail);

        assertNotNull(foundUser);
        assertFalse(foundUser, "Repository should return false for a non-existent username");
    }
}
