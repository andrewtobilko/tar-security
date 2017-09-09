package com.tobilko.data.storage;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.Account;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleAccountStorageProvider implements AccountStorage {

    private final List<Account> accounts;

    @Setter
    @Inject
    private EventBus eventBus;

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
        eventBus.post(new Event(EventType.ACCOUNT_LIST_CHANGED, account));
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

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accounts);
    }

    private void fillUpAccountWithAnotherAccount(Account account, Account anotherAccount) {
        account.setPassword(anotherAccount.getPassword());
    }

}
