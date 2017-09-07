package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by Andrew Tobilko on 9/7/17.
 */
@Singleton
public final class GridPaneController {

    private final EventBus eventBus;

    @Inject
    public GridPaneController(EventBus eventBus) {
        eventBus.register(this);
        this.eventBus = eventBus;
    }

}
