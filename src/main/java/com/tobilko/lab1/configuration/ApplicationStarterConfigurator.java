package com.tobilko.lab1.configuration;

import com.google.inject.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;

import static com.tobilko.lab1.configuration.constant.WindowProperty.ApplicationWindow.*;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class ApplicationStarterConfigurator {

    private final FXMLLoader loader;

    @SneakyThrows
    public void configureApplicationWithPrimaryStage(Stage stage) {
        loader.setLocation(new File(PATH_TO_SCHEMA).toURI().toURL());

        stage.setScene(new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.setTitle(TITLE);
    }

}