package com.tobilko.lab1.controller;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor
public abstract class Controller implements EventBusAware {

    @Getter
    protected final EventBus eventBus;

}
