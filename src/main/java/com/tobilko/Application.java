package com.tobilko;

import com.tobilko.configuration.ApplicationStarterConfigurator;
import javafx.stage.Stage;

public final class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationStarterConfigurator().configureApplicationWithPrimaryStage(stage);

        stage.show();
    }

}
