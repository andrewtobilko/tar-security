package com.tobilko.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationStarterConfigurator {

    private final String PATH_TO_SCHEMA = "src/main/resources/application.fxml";
    private final String TITLE = "Andrew Tobilko [lab #1]";
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 500;

    @SneakyThrows
    public void configureApplicationWithPrimaryStage(Stage stage) {

        FXMLLoader loader = new FXMLLoader(new File(PATH_TO_SCHEMA).toURI().toURL());

        Injector injector = Guice.createInjector(new ApplicationModule());
        loader.setControllerFactory(injector::getInstance);

        stage.setScene(new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.setTitle(TITLE);
    }

}