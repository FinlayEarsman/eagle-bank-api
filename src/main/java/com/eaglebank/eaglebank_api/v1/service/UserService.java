package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.model.UserModel;

import java.util.Optional;

public interface UserService {
    UserModel createUser(UserModel user);
    Optional<UserModel> getUserById(String username, Long id);
    Optional<UserModel> getUserByEmail(String email);
    Optional<UserModel> updateUser(UserModel user);
    boolean deleteUser(String username, Long id);
}


