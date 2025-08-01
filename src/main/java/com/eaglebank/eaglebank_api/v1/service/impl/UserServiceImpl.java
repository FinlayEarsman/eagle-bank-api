package com.eaglebank.eaglebank_api.v1.service.impl;

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
    public Optional<UserModel> getUserById(Long id) {
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
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}