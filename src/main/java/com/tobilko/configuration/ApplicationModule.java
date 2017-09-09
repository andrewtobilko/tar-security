package com.tobilko.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.tobilko.controller.*;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.account.principal.storage.PrincipalStorageProvider;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import com.tobilko.data.storage.SimpleAccountStorageProvider;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        configureBinder();
        configureMultiBinder();
    }

    private void configureBinder() {
        bind(MenuController.class).asEagerSingleton();
        bind(ApplicationController.class).asEagerSingleton();
        bind(AuthorisationController.class).asEagerSingleton();
        bind(AccountListController.class).asEagerSingleton();

        bind(AccountStorage.class).toInstance(getAccountStorage());
        bind(EventBus.class).toInstance(new EventBus());

        bind(PrincipalStorage.class).to(PrincipalStorageProvider.class).asEagerSingleton();
    }

    // todo : remove
    private void configureMultiBinder() {
        Multibinder<Controller> controllerBinder =
                Multibinder.newSetBinder(binder(), Controller.class);

        controllerBinder.addBinding().to(MenuController.class);
        controllerBinder.addBinding().to(AccountStorageController.class);
        controllerBinder.addBinding().to(AuthorisationController.class);
    }

    private AccountStorage getAccountStorage() {
        return SimpleAccountStorageProvider.getAccountStorageWithInitialState(getInitialAccounts());
    }

    private List<Account> getInitialAccounts() {
        return ImmutableList.of(
                new Account("Andrew", "000", Role.ADMIN),
                new Account("Ann", "111", Role.ORDINARY_USER),
                new Account("Mike", "222", Role.ORDINARY_USER)
        );
    }

    @Singleton
    public EventBus getEventBus() {
        return new EventBus();
    }

}