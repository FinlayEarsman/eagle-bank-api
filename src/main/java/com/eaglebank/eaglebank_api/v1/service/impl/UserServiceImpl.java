package com.eaglebank.eaglebank_api.v1.service.impl;

import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.eaglebank.eaglebank_api.v1.dto.UserResponseDto;
import com.eaglebank.eaglebank_api.v1.dto.UserUpdateDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidFieldException;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.exception.UserDeleteForbiddenException;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.eaglebank.eaglebank_api.v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRegistrationDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new InvalidFieldException("email", "String", "Email already exists");
        }
        if (userDto.getInvalidField() != null) {
        throw new InvalidFieldException(userDto.getInvalidField(), "String", "Missing required data");
        }

        UserModel user = UserModel.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress() != null ? userDto.getAddress().toModel() : null)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        UserModel created = userRepository.save(user);
        return toDto(created);
    }

    @Override
    public Optional<UserResponseDto> getUserById(String username, Long id) {
        Optional<UserModel> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        UserModel user = foundUser.get();
        if (!user.getEmail().equals(username)) {
            throw new InvalidUserException("User does not match the provided username");
        }

        return Optional.of(toDto(foundUser.get()));
    }

    @Override
    public Optional<UserResponseDto> updateUser(String username, String id, UserUpdateDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new InvalidFieldException("email", "String", "Email already exists");
        }
        if (userDto.getInvalidField() != null) {
            throw new InvalidFieldException(userDto.getInvalidField(),userDto.getInvalidField().equals("address") ? "Object" : "String", "Missing required data");
        }

        UserModel foundUser = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new InvalidUserException("User not found with id: " + id));
        if (!foundUser.getEmail().equals(username)) {
            throw new InvalidUserException("User does not match the provided username");
        }
        foundUser.setName(userDto.getName());
        foundUser.setPhoneNumber(userDto.getPhoneNumber());
        foundUser.setAddress(userDto.getAddress() != null ? userDto.getAddress().toModel() : null);
        foundUser.setEmail(userDto.getEmail());

        return Optional.of(toDto(userRepository.save(foundUser)));
    }

    @Override
    public boolean deleteUser(String username, Long id) {
        validateUser(username);

        Optional<UserModel> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return false;
        }
        UserModel user = foundUser.get();
        if (!user.getEmail().equals(username)) {
            throw new InvalidUserException("User does not match the provided username");
        }
        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            throw new UserDeleteForbiddenException("Cannot delete user with existing accounts");
        }

        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<UserResponseDto> getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        return Optional.ofNullable(toDto(user));
    }

    private UserResponseDto toDto(UserModel user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .createdTimestamp(String.valueOf(user.getCreatedTimestamp()))
                .updatedTimestamp(String.valueOf(user.getUpdatedTimestamp()))
                .address(user.getAddress() != null ? user.getAddress().toDto() : null)
                .build();
    }

    private void validateUser(String username) {
        UserResponseDto user = this.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("Invalid user: " + username));

        if (!Objects.equals(user.getEmail(), username)) {
            throw new InvalidUserException("User does not match logged in user");
        }
    }
}