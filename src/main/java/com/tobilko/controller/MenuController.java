package com.tobilko.controller;

import com.google.inject.Singleton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;


/**
 * Created by Andrew Tobilko on 9/7/17.
 */
@Singleton
public final class MenuController implements Controller {

    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        new Alert(
                Alert.AlertType.INFORMATION,
                DeveloperInformation.DEVELOPED_BY_TITLE
        ).show();

    }

    private class DeveloperInformation {

        private static final String DEVELOPED_BY_TITLE = "Designed by Andrew Tobilko, 2017";

    }

}
