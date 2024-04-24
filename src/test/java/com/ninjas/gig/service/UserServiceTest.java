package com.ninjas.gig.service;

import com.ninjas.gig.dto.UserInfoDto;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testChangeUserPhoto_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        String newPhotoUrl = "http://example.com/new-photo.jpg";
        UserAccount user = new UserAccount();
        user.setId(userId);
        user.setPhoto("http://example.com/old-photo.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.changeUserPhoto(userId, newPhotoUrl);

        // Assert
        assertEquals(newPhotoUrl, user.getPhoto());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangeUserPhoto_WhenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        String newPhotoUrl = "http://example.com/new-photo.jpg";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        userService.changeUserPhoto(userId, newPhotoUrl);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testGetUserById_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        UserAccount expectedUser = new UserAccount();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        UserAccount actualUser = userService.getUserById(userId);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserById_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(userId));
        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    public void testGetUserDetails_WhenUserExists() {
        Long userId = 1L;
        UserAccount user = new UserAccount();
        user.setId(userId);
        user.setPhoto("photo.jpg");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserInfoDto userDetails = userService.getUserDetails(userId);

        assertNotNull(userDetails);
        assertEquals(userId, userDetails.getId());
        assertEquals("photo.jpg", userDetails.getPhoto());
        assertEquals("John Doe", userDetails.getName());
        assertEquals("john.doe@example.com", userDetails.getEmail());
        assertEquals("johndoe", userDetails.getUsername());
    }

    @Test
    public void testGetUserDetails_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserInfoDto userDetails = userService.getUserDetails(userId);

        assertNull(userDetails);
    }
}
