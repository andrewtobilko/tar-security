package com.tobilko.data.storage;

import com.google.inject.Inject;
import com.tobilko.configuration.file.JsonFileService;
import com.tobilko.data.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class AccountStorageProvider implements AccountStorage {

    private final List<Account> accounts;
    private final PasswordEncoder encoder;

    @Override
    public Optional<Account> getAccountByName(String name) {
        return accounts.stream().filter(a -> a.getName().equals(name)).findAny();
    }

    @Override
    public void removeAccount(Account account) {
        synchroniseJsonFileAfterActionOverAccount(accounts::remove, account);
    }

    @Override
    public void saveAccount(Account account) {
        synchroniseJsonFileAfterActionOverAccount(acc -> {

            acc.setPassword(encoder.encode(acc.getPassword()));
            accounts.add(acc);

        }, account);
    }

    @Override
    public void updateAccount(Account account) {
        synchroniseJsonFileAfterActionOverAccount(
                acc -> getAccountByName(account.getName())
                        .ifPresent(a -> fillUpAccountWithAnotherAccount(a, acc)),
                account
        );
    }

    private void synchroniseJsonFileAfterActionOverAccount(Consumer<Account> consumer, Account account) {
        consumer.accept(account);
        JsonFileService.serialiseAccounts(accounts);
    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accounts);
    }

    private void fillUpAccountWithAnotherAccount(Account account, Account anotherAccount) {
        account.setPassword(encoder.encode(anotherAccount.getPassword()));
        account.setBlocked(anotherAccount.isBlocked());
    }

}
