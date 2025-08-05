package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.dto.*;
import com.eaglebank.eaglebank_api.v1.exception.*;
import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.eaglebank.eaglebank_api.v1.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserShouldCreateUserWhenValid() {
        UserRegistrationDto dto = mock(UserRegistrationDto.class);
        when(dto.getEmail()).thenReturn("test@example.com");
        when(dto.isValid()).thenReturn(true);
        when(dto.getPassword()).thenReturn("pass");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        UserModel userModel = UserModel.builder().email("test@example.com").build();
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserResponseDto result = userService.createUser(dto);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void createUserThrowsWhenEmailExists() {
        UserRegistrationDto dto = mock(UserRegistrationDto.class);
        when(dto.getEmail()).thenReturn("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(new UserModel());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    @Test
    void getUserByIdShouldReturnUserWhenMatches() {
        UserModel user = UserModel.builder().id(1L).email("test@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDto> result = userService.getUserById("test@example.com", 1L);
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void getUserByIdThrowsWhenEmailMismatch() {
        UserModel user = UserModel.builder().id(1L).email("other@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(InvalidUserException.class, () -> userService.getUserById("test@example.com", 1L));
    }

    @Test
    void updateUserShouldUpdateWhenMatches() {
        UserUpdateDto updateDto = mock(UserUpdateDto.class);
        when(updateDto.isValid()).thenReturn(true);
        when(updateDto.getName()).thenReturn("New Name");
        when(updateDto.getEmail()).thenReturn("test@example.com");

        UserModel user = UserModel.builder().id(1L).email("test@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        Optional<UserResponseDto> result = userService.updateUser("test@example.com", "1", updateDto);
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void deleteUserShouldDeleteWhenNoAccounts() {
        UserModel user = UserModel.builder().id(1L).email("test@example.com").accounts(Collections.emptyList()).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        boolean result = userService.deleteUser("test@example.com", 1L);
        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserThrowsWhenUserHasAccounts() {
        UserModel user = UserModel.builder().id(1L).email("test@example.com").accounts(List.of(AccountModel.builder().build())).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertThrows(UserDeleteForbiddenException.class, () -> userService.deleteUser("test@example.com", 1L));
    }
}