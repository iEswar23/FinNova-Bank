package com.eswar.finnova.data.repository;

import com.eswar.finnova.data.model.Account;

import java.util.List;

public interface AccountRepository {
    void getAccounts(DataCallback<List<Account>> callback);
}
