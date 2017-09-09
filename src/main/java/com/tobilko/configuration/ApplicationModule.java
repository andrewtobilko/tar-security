package com.tobilko.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.tobilko.controller.AccountListController;
import com.tobilko.controller.ApplicationController;
import com.tobilko.controller.AuthorisationController;
import com.tobilko.controller.MenuController;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.account.principal.storage.PrincipalStorageProvider;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import com.tobilko.data.storage.SimpleAccountStorageProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationModule extends AbstractModule {

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    protected void configure() {
        bind(MenuController.class).asEagerSingleton();
        bind(ApplicationController.class).asEagerSingleton();
        bind(AuthorisationController.class).asEagerSingleton();
        bind(AccountListController.class).asEagerSingleton();

        bind(AccountStorage.class).toInstance(getAccountStorage());
        bind(EventBus.class).toInstance(new EventBus());
        bind(PasswordEncoder.class).toInstance(encoder);

        bind(PrincipalStorage.class).to(PrincipalStorageProvider.class).asEagerSingleton();
    }

    private AccountStorage getAccountStorage() {
        return SimpleAccountStorageProvider.getAccountStorageWithInitialState(getInitialAccounts());
    }

    private List<Account> getInitialAccounts() {
        return ImmutableList.of(
                new Account("Andrew", encoder.encode("000"), Role.ADMIN_ACCOUNT),
                new Account("Ann", encoder.encode("111"), Role.ORDINARY_ACCOUNT),
                new Account("Mike", encoder.encode("222"), Role.ORDINARY_ACCOUNT)
        );
    }

}