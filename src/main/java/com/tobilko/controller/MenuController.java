package com.tobilko.controller;

import com.google.inject.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;


/**
 * Created by Andrew Tobilko on 9/7/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class MenuController implements Controller {

    private final Injector injector;

    @FXML
    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        new Alert(
                Alert.AlertType.INFORMATION,
                "Designed by Andrew Tobilko, 2017"
        ).show();

        System.out.println(injector.getAllBindings());

        for (Map.Entry<Key<?>, Binding<?>> entry : injector.getAllBindings().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

}
