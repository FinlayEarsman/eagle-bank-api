package com.eaglebank.eaglebank_api.v1.repository;

import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import com.eaglebank.eaglebank_api.v1.model.TransactionModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<TransactionModel, String> {
    List<TransactionModel> findAllByAccount(AccountModel accountModel);
}
