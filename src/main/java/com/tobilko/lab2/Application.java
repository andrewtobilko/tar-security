package com.tobilko.lab2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import static com.tobilko.lab2.configuration.ConfigurationConstant.PATH_TO_SCHEMA;
import static com.tobilko.lab2.configuration.ConfigurationConstant.TITLE;

/**
 * Created by Andrew Tobilko on 9/23/17.
 */
public final class Application extends javafx.application.Application {

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        launch(args);
    }

    private static void configureGlobalSecurityConfiguration() {
        Security.addProvider(new BouncyCastleProvider()); // TODO: 10/8/17
    }

    @Override
    public void start(Stage stage) throws Exception {
        configureGlobalSecurityConfiguration();

        FXMLLoader loader = new FXMLLoader(new File(PATH_TO_SCHEMA).toURI().toURL());

        stage.setScene(new Scene(loader.load()));
        stage.setTitle(TITLE);

        stage.show();
    }

}
