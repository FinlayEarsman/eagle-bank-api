package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.eaglebank.eaglebank_api.v1.dto.UserResponseDto;
import com.eaglebank.eaglebank_api.v1.dto.UserUpdateDto;

import java.util.Optional;

public interface UserService {
    UserResponseDto createUser(UserRegistrationDto user);
    Optional<UserResponseDto> getUserById(String username, Long id);
    Optional<UserResponseDto> getUserByEmail(String email);
    Optional<UserResponseDto> updateUser(String username, String id, UserUpdateDto user);
    boolean deleteUser(String username, Long id);
}


