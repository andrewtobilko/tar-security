package com.tobilko.lab1.data.storage;

import com.tobilko.lab1.data.account.Account;

import java.util.List;
import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public interface AccountStorage {

    List<Account> getAll();

    Optional<Account> getAccountByName(String name);

    void removeAccount(Account account);

    void saveAccount(Account account);

    void updateAccount(Account account);

}
