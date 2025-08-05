package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.eaglebank.eaglebank_api.v1.dto.UserResponseDto;
import com.eaglebank.eaglebank_api.v1.dto.UserUpdateDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.exception.UserDeleteForbiddenException;
import com.eaglebank.eaglebank_api.v1.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRegistrationDto userDto) {
        UserResponseDto createdUser;
        try {
            createdUser = userService.createUser(userDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserResponseDto foundUser;
        try {
            foundUser = userService.getUserById(currentUsername, Long.valueOf(id))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user"));
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access other user's data");
        }

         return ResponseEntity.ok(foundUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody UserUpdateDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserResponseDto updatedUser;
        try {
            updatedUser = userService.updateUser(currentUsername, id, userDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user"));
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update other user's data");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data: " + e.getMessage());
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        try {
            if (!userService.deleteUser(currentUsername, Long.valueOf(id))) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user with id: " + id);
            }
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete other user's data");
        } catch (UserDeleteForbiddenException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete user with existing accounts");
        }

        return ResponseEntity.noContent().build();
    }
}