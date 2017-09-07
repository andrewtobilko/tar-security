package com.tobilko.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Created by Andrew Tobilko on 9/7/17.
 */
public final class MainController {

    @FXML
    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        new Alert(
                Alert.AlertType.INFORMATION,
                "Designed by Andrew Tobilko, 2017"
        ).show();

    }

}
