package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.tobilko.data.storage.AccountStorage;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountStorageController {

    private final AccountStorage storage;
    private final EventBus eventBus;

}
