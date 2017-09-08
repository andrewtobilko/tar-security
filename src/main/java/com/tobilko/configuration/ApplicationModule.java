package com.tobilko.configuration;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tobilko.controller.MainController;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MainController.class).asEagerSingleton();
        //bind(AccountStorage.class).to(SimpleAccountStorageProvider.class).asEagerSingleton();
    }

    @Singleton
    EventBus getEventBus() {
        return new EventBus();
    }

}