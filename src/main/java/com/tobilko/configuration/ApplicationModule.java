package com.tobilko.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.tobilko.controller.AccountStorageController;
import com.tobilko.controller.Controller;
import com.tobilko.controller.MenuController;
import com.tobilko.data.account.Account;
import com.tobilko.data.storage.AccountStorage;
import com.tobilko.data.storage.SimpleAccountStorageProvider;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MenuController.class).asEagerSingleton();
        bind(AccountStorage.class).toInstance(getAccountStorage());

        Multibinder<Controller> controllerBinder =
                Multibinder.newSetBinder(binder(), Controller.class);

        controllerBinder.addBinding().to(MenuController.class);
        controllerBinder.addBinding().to(AccountStorageController.class);
    }

    private AccountStorage getAccountStorage() {
        return SimpleAccountStorageProvider.getAccountStorageWithInitialState(getInitialAccounts());
    }

    private List<Account> getInitialAccounts() {
        return ImmutableList.of(
                new Account("Andrew", "000"),
                new Account("Ann", "111"),
                new Account("Mike", "222")
        );
    }

    @Singleton
    public EventBus getEventBus() {
        return new EventBus();
    }

}