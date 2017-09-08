package com.tobilko.data.storage;

import com.tobilko.data.account.Account;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public interface AccountStorage {

    Optional<Account> getAccountByName(String name);

    void removeAccount(Account account);

    void saveAccount(Account account);

    void updateAccount(Account account);

}
