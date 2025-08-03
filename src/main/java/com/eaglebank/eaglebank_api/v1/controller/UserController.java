package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.eaglebank.eaglebank_api.v1.dto.UserResponseDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/v1/users")
public class UserController {

   // TODO: move all DTO conversions to user service layer
    @Autowired
    private UserServiceImpl userService;

    //TODO: validate user data before saving
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRegistrationDto userDto) {
        UserModel user = UserModel.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress() != null ? userDto.getAddress().toModel() : null)
                .password(userDto.getPassword())
                .build();

        UserModel newUser = userService.createUser(user);
        return UserResponseDto.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .name(newUser.getName())
                .phoneNumber(newUser.getPhoneNumber())
                .createdTimestamp(String.valueOf(newUser.getCreatedTimestamp()))
                .updatedTimestamp(String.valueOf(newUser.getUpdatedTimestamp()))
                .address(newUser.getAddress() != null ? newUser.getAddress().toDto() : null)
                .build();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserModel foundUser;
        try {
            foundUser = userService.getUserById(currentUsername, Long.valueOf(id))
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find user"));
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access other user's data");
        }

         return UserResponseDto.builder()
                 .id(foundUser.getId())
                 .email(foundUser.getEmail())
                 .name(foundUser.getName())
                 .phoneNumber(foundUser.getPhoneNumber())
                 .createdTimestamp(String.valueOf(foundUser.getCreatedTimestamp()))
                 .updatedTimestamp(String.valueOf(foundUser.getUpdatedTimestamp()))
                 .address(foundUser.getAddress() != null ? foundUser.getAddress().toDto() : null)
                 .build();
    }

    // TODO: validate user data before updating
    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable String id, @RequestBody UserRegistrationDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserModel foundUser;
        try {
            foundUser = userService.getUserById(currentUsername, Long.valueOf(id))
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find user"));
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update other user's data");
        }

        UserModel user = UserModel.builder()
                .id(Long.valueOf(id))
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress() != null ? userDto.getAddress().toModel() : null)
                .password(foundUser.getPassword())
                .build();

        UserModel updatedUser = userService.updateUser(user).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find user"));
        return UserResponseDto.builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .phoneNumber(updatedUser.getPhoneNumber())
                .createdTimestamp(String.valueOf(updatedUser.getCreatedTimestamp()))
                .updatedTimestamp(String.valueOf(updatedUser.getUpdatedTimestamp()))
                .address(updatedUser.getAddress() != null ? updatedUser.getAddress().toDto() : null)
                .build();
    }

    // TODO: implement so user can only delete their data if they don't have any accounts
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        try {
            if (!userService.deleteUser(currentUsername, Long.valueOf(id))) {
                throw new ResponseStatusException(NOT_FOUND, "Unable to find user with id: " + id);
            }
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete other user's data");
        }

        return ResponseEntity.noContent().build();
    }
}