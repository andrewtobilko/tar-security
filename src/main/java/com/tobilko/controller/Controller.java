package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor
public abstract class Controller implements EventBusAware {

    @Getter(AccessLevel.PROTECTED)
    protected final EventBus eventBus;

}
