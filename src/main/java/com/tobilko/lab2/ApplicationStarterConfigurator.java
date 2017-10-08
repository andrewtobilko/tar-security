package com.tobilko.lab2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.Security;

import static com.tobilko.lab2.configuration.ConfigurationConstant.PATH_TO_SCHEMA;
import static com.tobilko.lab2.configuration.ConfigurationConstant.TITLE;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class ApplicationStarterConfigurator {

    @SneakyThrows
    public static void configureApplicationWithPrimaryStage(Stage stage) {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File(PATH_TO_SCHEMA).toURI().toURL());

        stage.setScene(new Scene(loader.load()));
        stage.setTitle(TITLE);
    }

    public static void configureGlobalSecurityConfiguration() {
        Security.addProvider(new BouncyCastleProvider());
    }

}