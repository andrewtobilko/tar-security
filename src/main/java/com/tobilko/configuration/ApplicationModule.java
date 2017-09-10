package com.tobilko.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
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
import javafx.fxml.FXMLLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.tobilko.data.role.Role.*;

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

        // PasswordEncoder dependencies
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        bind(PasswordEncoder.class).toInstance(encoder);
        bind(AccountStorage.class).toInstance(getAccountStorage(encoder));

        bind(PrincipalStorage.class).to(PrincipalStorageProvider.class).asEagerSingleton();
    }

    @Provides
    public FXMLLoader getConfiguredFXMLLoader(Injector injector) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }

        private AccountStorage getAccountStorage(PasswordEncoder encoder) {
            return SimpleAccountStorageProvider.getAccountStorageWithInitialState(getInitialAccounts(encoder));
        }

        private List<Account> getInitialAccounts(PasswordEncoder encoder) {
            return ImmutableList.of(
                    new Account("Andrew", encoder.encode("000"), ADMIN_ACCOUNT, false),
                    new Account("Ann", encoder.encode("111"), ORDINARY_ACCOUNT, false),
                    new Account("Mike", encoder.encode("222"), ORDINARY_ACCOUNT, false)
            );
        }

}