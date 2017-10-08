package com.tobilko.lab2;

import javafx.stage.Stage;

import static com.tobilko.lab2.ApplicationStarterConfigurator.configureApplicationWithPrimaryStage;
import static com.tobilko.lab2.ApplicationStarterConfigurator.configureGlobalSecurityConfiguration;

/**
 * Created by Andrew Tobilko on 9/23/17.
 */
public final class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        configureGlobalSecurityConfiguration();
        configureApplicationWithPrimaryStage(stage);
    }

}
