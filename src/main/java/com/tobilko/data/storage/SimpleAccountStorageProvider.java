package com.tobilko.data.storage;

import com.tobilko.data.account.Account;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleAccountStorageProvider implements AccountStorage {

    private final List<Account> accounts;

    public static AccountStorage getAccountStorageWithInitialState(List<Account> state) {
        return new SimpleAccountStorageProvider(new ArrayList<>(state));
    }

    @Override
    public Optional<Account> getAccountByName(String name) {
        return accounts.stream().filter(a -> a.getName().equals(name)).findAny();
    }

    @Override
    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    @Override
    public void saveAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public void updateAccount(Account account) {
        getAccountByName(account.getName())
                .ifPresent(a -> fillUpAccountWithAnotherAccount(a, account));
    }

    private void fillUpAccountWithAnotherAccount(Account account, Account anotherAccount) {
        account.setPassword(anotherAccount.getPassword());
        // todo
    }

}
