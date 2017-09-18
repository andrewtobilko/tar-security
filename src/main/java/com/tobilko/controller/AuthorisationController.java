package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;

import static com.tobilko.configuration.constant.WindowProperty.AuthorisationWindow.*;
import static com.tobilko.configuration.event.EventType.PRINCIPAL_CHANGED;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public class AuthorisationController extends Controller {

    private @FXML Button logOutButton;
    private @FXML Button logInButton;

    private final FXMLLoader loader;
    private final PrincipalStorage principalStorage;

    @Inject
    public AuthorisationController(EventBus eventBus, FXMLLoader loader, PrincipalStorage principalStorage) {
        super(eventBus);

        this.loader = loader;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @FXML
    public void handleLogInAction(ActionEvent event) {
        renderLogInWindow();
    }

    @SneakyThrows
    private void renderLogInWindow() {
        Stage stage = new Stage();

        loader.setLocation(new File(PATH_TO_SCHEMA).toURI().toURL());

        cleanUPFXMLLoader(loader);

        stage.setTitle(TITLE);
        stage.setScene(new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }

    private void cleanUPFXMLLoader(FXMLLoader loader) {
        loader.setController(null);
        loader.setRoot(null);
    }

    @FXML
    public void handleLogOutAction(ActionEvent event) {
        principalStorage.clearStorage();
        eventBus.post(new Event(PRINCIPAL_CHANGED, null));
    }

    @Subscribe
    public void handleEvent(Event event) {

        if (event.getType().equals(PRINCIPAL_CHANGED)) {

            boolean isPrincipalPresent = principalStorage.getPrincipal().isPresent();

            logOutButton.setDisable(!isPrincipalPresent);
            logInButton.setDisable(isPrincipalPresent);

        }

    }

}
