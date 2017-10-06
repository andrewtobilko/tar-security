package com.tobilko.lab1.configuration.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Data
@RequiredArgsConstructor
public final class Event {

    private final EventType type;
    private final Object payload;

}
