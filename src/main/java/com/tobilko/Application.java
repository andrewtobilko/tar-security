package com.tobilko;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.tobilko.configuration.ApplicationModule;
import com.tobilko.configuration.ApplicationStarterConfigurator;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public final class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = createApplicationInjector();
        new ApplicationStarterConfigurator(injector.getInstance(FXMLLoader.class)).configureApplicationWithPrimaryStage(stage);

        stage.show();
    }

    private Injector createApplicationInjector() {
        return Guice.createInjector(new ApplicationModule());
    }

}
