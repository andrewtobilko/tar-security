package com.tobilko.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;

import static com.tobilko.configuration.constant.WindowProperty.ApplicationWindow.*;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class ApplicationStarterConfigurator {

    private final FXMLLoader loader;

    @SneakyThrows
    public void configureApplicationWithPrimaryStage(Stage stage) {

        //FXMLLoader loader = new FXMLLoader(new File(PATH_TO_SCHEMA).toURI().toURL());
        loader.setLocation(new File(PATH_TO_SCHEMA).toURI().toURL());
//        Injector injector = Guice.createInjector(new ApplicationModule());
//        loader.setControllerFactory(injector::getInstance);

        stage.setScene(new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.setTitle(TITLE);
    }

}