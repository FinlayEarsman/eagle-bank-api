package com.eaglebank.eaglebank_api.v1.service.impl;

import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.eaglebank.eaglebank_api.v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel createUser(UserModel user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<UserModel> getUserById(String username, Long id) {
        Optional<UserModel> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        UserModel user = foundUser.get();
        if (!user.getEmail().equals(username)) {
            throw new InvalidUserException("User does not match the provided username");
        }

        return userRepository.findById(id);
    }

    @Override
    public Optional<UserModel> updateUser(UserModel user) {
        if (!userRepository.existsById(user.getId())) {
            return Optional.empty();
        }
        return Optional.of(userRepository.save(user));
    }

    @Override
    public boolean deleteUser(String username, Long id) {

        Optional<UserModel> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return false;
        }
        UserModel user = foundUser.get();
        if (!user.getEmail().equals(username)) {
            throw new InvalidUserException("User does not match the provided username");
        }

        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<UserModel> getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        return Optional.ofNullable(user);
    }
}