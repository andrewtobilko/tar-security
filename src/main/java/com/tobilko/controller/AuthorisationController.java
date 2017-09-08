package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.storage.AccountStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Optional;

import static com.tobilko.configuration.alert.AlertUtils.showAlertWithTypeAndMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public class AuthorisationController implements Controller {

    private final EventBus eventBus;
    private final Injector injector;
    private final AccountStorage accountStorage;
    private final PrincipalStorage principalStorage;

    private @FXML TextField nameField;
    private @FXML TextField passwordField;

    @Inject
    public AuthorisationController(
            EventBus eventBus,
            Injector injector,
            AccountStorage accountStorage,
            PrincipalStorage principalStorage
    ) {
        this.eventBus = eventBus;
        this.injector = injector;
        this.accountStorage = accountStorage;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @FXML
    public void handleLogInAction(ActionEvent event) {
        FXMLLoader loader = configureLogInWindowLoader();
        renderLogInWindowWithFXMLLoader(loader);
    }

    @SneakyThrows
    private FXMLLoader configureLogInWindowLoader() {
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/authorisation-window.fxml").toURI().toURL());
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }

    @SneakyThrows
    private void renderLogInWindowWithFXMLLoader(FXMLLoader loader) {
        final int WIDTH = 400;
        final int HEIGHT = 200;
        final String TITLE = "Login";

        Stage stage = new Stage();

        stage.setTitle(TITLE);
        stage.setScene(new Scene(loader.load(), WIDTH, HEIGHT));
        stage.show();
    }

    @FXML
    public void handleLogOutAction(ActionEvent event) {
        principalStorage.clearStorage();
        eventBus.post(new Event(EventType.PRINCIPAL_CHANGED, null));
    }

    @FXML
    public void validateCredentials(ActionEvent event) {
        String name = nameField.getText();
        String password = passwordField.getText();

        Optional<Account> optionalAccount = accountStorage.getAccountByName(name);
        Account account;

        if (optionalAccount.isPresent() && (account = optionalAccount.get()).getPassword().equals(password)) {
            // handle successful validation

            principalStorage.putPrincipalIntoStorage(account);
            closeAuthorisationWindow(event);
            eventBus.post(new Event(EventType.PRINCIPAL_CHANGED, account));

            showAlertWithTypeAndMessage(INFORMATION, "You have successfully logged in!");
        } else {
            // handle failed validation

            showAlertWithTypeAndMessage(ERROR, "You have entered an incorrect credentials!");
        }
    }

    @FXML
    public void closeAuthorisationWindow(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private Button logOutButton;
    @FXML
    private Button logInButton;

    @Subscribe
    public void handleIncomingEvent(Event event) {
        if (event.getType().equals(EventType.PRINCIPAL_CHANGED)) {
            boolean isPrincipalPresent = principalStorage.getPrincipal().isPresent();
            logOutButton.setDisable(!isPrincipalPresent);
            logInButton.setDisable(isPrincipalPresent);
        }
    }

}
