package com.eaglebank.eaglebank_api.v1.repository;

import com.eaglebank.eaglebank_api.v1.model.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByEmail(String username);
}
