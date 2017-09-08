package com.tobilko.configuration.alert;

import javafx.scene.control.Alert;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
public final class AlertUtils {

    public static void showAlertWithTypeAndMessage(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }
}
