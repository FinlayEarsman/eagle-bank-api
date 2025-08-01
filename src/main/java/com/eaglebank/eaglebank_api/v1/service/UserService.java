package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.model.UserModel;

import java.util.Optional;

public interface UserService {
    UserModel createUser(UserModel user);
    Optional<UserModel> getUserById(Long id);
    Optional<UserModel> updateUser(UserModel user);
    boolean deleteUser(Long id);
}


