package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.eaglebank.eaglebank_api.v1.dto.UserResponseDto;
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
    @Autowired
    private UserServiceImpl userService;

    //TODO: validate user data before saving
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRegistrationDto userDto) {
        UserModel user = new UserModel();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFullName(userDto.getFullName());
        UserModel newUser = userService.createUser(user);
        return UserResponseDto.builder().id(newUser.getId()).email(newUser.getEmail()).fullName(newUser.getFullName()).build();
    }

    //TODO: implement so user can only see their own data
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

         UserModel foundUser = userService.getUserById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find user"));

        if (!foundUser.getEmail().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access other user's data");
        }
         return UserResponseDto.builder()
                .id(foundUser.getId())
                .email(foundUser.getEmail())
                .fullName(foundUser.getFullName())
                .build();
    }

    //TODO: implement so user can only update their own data
    @PatchMapping
    public UserResponseDto updateUser(@RequestBody UserRegistrationDto userDto) {
        UserModel user = new UserModel();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFullName(userDto.getFullName());
        UserModel updatedUser = userService.updateUser(user).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find user with id: " + user.getId()));
        return UserResponseDto.builder().id(updatedUser.getId()).email(updatedUser.getEmail()).fullName(updatedUser.getFullName()).build();

    }

    // TODO: implement so user can only delete their own data
    // TODO: implement so user can only delete their data if they don't have any accounts
    @DeleteMapping
    public String deleteUser(@RequestParam Long id) {
        if (!userService.deleteUser(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find user with id: " + id);
        }
        return "User with id: " + id + " deleted successfully.";
    }

}