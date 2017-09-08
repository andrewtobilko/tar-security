package com.tobilko.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class MainController implements Controller {

    private final List<Controller> controllers;



}
