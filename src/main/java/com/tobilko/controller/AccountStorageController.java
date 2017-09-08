package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.data.storage.AccountStorage;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class AccountStorageController implements Controller {

    private final AccountStorage storage;
    private final EventBus eventBus;

}
