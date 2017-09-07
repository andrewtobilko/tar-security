package com.tobilko;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tobilko.controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;

public class Application
        extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationStarterConfigurator().configureApplicationWithPrimaryStage(stage);

        stage.show();
    }

    private class ApplicationStarterConfigurator {

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

    private class ApplicationModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(MainController.class).asEagerSingleton();
        }

        //@Provides
        @Singleton
        EventBus getEventBus() {
            return new EventBus();
        }

    }

}
