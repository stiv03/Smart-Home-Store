package com.ninjas.gig.repository;

import com.ninjas.gig.entity.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void findByUsername_ExistingUsername_ReturnsUserAccount() {
        // Arrange
        String username = "testUser";
        UserAccount expectedUser = new UserAccount();
        expectedUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        // Act
        UserAccount actualUser = userRepository.findByUsername(username);

        // Assert
        assertNotNull(actualUser);
        assertEquals(username, actualUser.getUsername());
    }

    @Test
    void findByUsername_NonExistingUsername_ReturnsNull() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        UserAccount actualUser = userRepository.findByUsername(username);

        // Assert
        assertNull(actualUser);
    }

    @Test
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userRepository.existsByEmail(email);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsByEmail_NonExistingEmail_ReturnsFalse() {
        // Arrange
        String email = "nonExisting@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userRepository.existsByEmail(email);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsByUsername_ExistingUsername_ReturnsTrue() {
        // Arrange
        String username = "testUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userRepository.existsByUsername(username);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsByUsername_NonExistingUsername_ReturnsFalse() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = userRepository.existsByUsername(username);

        // Assert
        assertFalse(result);
    }
}