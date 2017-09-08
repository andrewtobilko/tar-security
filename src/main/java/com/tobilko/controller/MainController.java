package com.tobilko.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class MainController implements Controller {

    private final Set<Controller> controllers;

    @FXML
    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        Controller next = controllers.iterator().next();
        MenuController menuController = (MenuController) next;

        menuController.handleDeveloperInformationButtonClick(event);
    }

}
