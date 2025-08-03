package com.eaglebank.eaglebank_api.v1.repository;

import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<AccountModel, Long> {
    List<AccountModel> findAllByUserId(Long id);
}
