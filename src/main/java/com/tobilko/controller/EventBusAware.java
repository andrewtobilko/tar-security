package com.tobilko.controller;

import com.google.common.eventbus.EventBus;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
public interface EventBusAware {

    EventBus getEventBus();

}
