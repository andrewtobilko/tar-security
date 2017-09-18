package com.tobilko.configuration;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.tobilko.configuration.file.JsonFileService;
import com.tobilko.controller.AccountListController;
import com.tobilko.controller.ApplicationController;
import com.tobilko.controller.AuthorisationController;
import com.tobilko.controller.MenuController;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.account.principal.storage.PrincipalStorageProvider;
import com.tobilko.data.storage.AccountStorage;
import com.tobilko.data.storage.AccountStorageProvider;
import javafx.fxml.FXMLLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        // controllers
        bind(MenuController.class).asEagerSingleton();
        bind(ApplicationController.class).asEagerSingleton();
        bind(AuthorisationController.class).asEagerSingleton();
        bind(AccountListController.class).asEagerSingleton();

        // EventBus
        bind(EventBus.class).toInstance(new EventBus());

        bind(PrincipalStorage.class).to(PrincipalStorageProvider.class).asEagerSingleton();
    }

    @Singleton
    @Provides
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Singleton
    @Provides
    public FXMLLoader getConfiguredFXMLLoader(Injector injector) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }

    @Provides
    @Singleton
    public AccountStorage getAccountStorage(PasswordEncoder encoder) {
        return new AccountStorageProvider(JsonFileService.deserialiseAccounts(), encoder);
    }

}