package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.storage.AccountStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.Optional;

import static com.tobilko.configuration.constant.WindowProperty.AuthorisationWindow.*;
import static com.tobilko.configuration.event.EventType.PRINCIPAL_CHANGED;
import static com.tobilko.configuration.file.FileSaver.saveAccountDetailsToFile;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public class AuthorisationController extends Controller {

    private final Injector injector;
    private final PrincipalStorage principalStorage;

    @Inject
    public AuthorisationController(
            EventBus eventBus,
            Injector injector,
            PrincipalStorage principalStorage
    ) {
        super(eventBus);

        this.injector = injector;
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
            // todo : get configured loader
            FXMLLoader loader = new FXMLLoader(new File(PATH_TO_SCHEMA).toURI().toURL());
            loader.setControllerFactory(injector::getInstance);

            return loader;
        }

        @SneakyThrows
        private void renderLogInWindowWithFXMLLoader(FXMLLoader loader) {
            Stage stage = new Stage();

            stage.setTitle(TITLE);
            stage.setScene(new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT));
            stage.show();
        }

    @FXML
    public void handleLogOutAction(ActionEvent event) {
        principalStorage.clearStorage();
        eventBus.post(new Event(PRINCIPAL_CHANGED, null));
    }

    private @FXML Button logOutButton;
    private @FXML Button logInButton;

    @Subscribe
    public void handleEvent(Event event) {

        if (event.getType().equals(PRINCIPAL_CHANGED)) {

            boolean isPrincipalPresent = principalStorage.getPrincipal().isPresent();

            logOutButton.setDisable(!isPrincipalPresent);
            logInButton.setDisable(isPrincipalPresent);

        }

    }

    @Singleton
    public static class AuthorisationWindowController extends Controller {

        private @FXML TextField nameField;
        private @FXML TextField passwordField;


        private final AccountStorage accountStorage;
        private final PrincipalStorage principalStorage;
        private final PasswordEncoder encoder;

        @Inject
        public AuthorisationWindowController(EventBus eventBus, PrincipalStorage principalStorage, AccountStorage accountStorage, PasswordEncoder encoder) {
            super(eventBus);

            this.principalStorage = principalStorage;
            this.accountStorage = accountStorage;
            this.encoder = encoder;
        }

        @FXML
        public void validateCredentials(ActionEvent event) {
            final String name = nameField.getText();
            final String password = passwordField.getText();

            Optional<Account> optionalAccount = accountStorage.getAccountByName(name);


            if (!optionalAccount.isPresent()) {
                showIncorrectCredentialsWindow();
                return;
            }

            Account account = optionalAccount.get();

            if (isPasswordsMatch(account.getPassword(), password)) {
                // handle successful validation

                if (validateAccountBlocking(account)) {
                    return;
                }

                savePrincipalIntoStorage(account);
                closeAuthorisationWindow(event);
                showAccountInformationAlert(account);
            } else {
                // handle failed validation
                showIncorrectCredentialsWindow();
            }

        }

            private void savePrincipalIntoStorage(Account account) {
                principalStorage.putPrincipalIntoStorage(account);
                notifyThatPrincipalHasBeenChanged(account);
            }

                private void notifyThatPrincipalHasBeenChanged(Account payload) {
                    eventBus.post(new Event(PRINCIPAL_CHANGED, payload));
                }

            private boolean isPasswordsMatch(String passwordHash, String incomingPassword) {
                return encoder.matches(incomingPassword, passwordHash);
            }

            private boolean validateAccountBlocking(Account account) {
                if (account.isBlocked()) {
                    new Alert(Alert.AlertType.ERROR, "That account is currently blocked...").show();

                    return true;
                }

                return false;
            }

            private void showAccountInformationAlert(Account account) {
                final ButtonType saveToFileButton = new ButtonType("Save to a file");

                new Alert(
                        INFORMATION,
                        "You have successfully logged in!\n\nDetailed account information:\n\n" + account.toFineString(),
                        saveToFileButton, ButtonType.OK
                )
                        .showAndWait()
                        .ifPresent(button -> {
                            if (button.equals(saveToFileButton)) {
                                if (saveAccountDetailsToFile(account)) {
                                    new Alert(INFORMATION, "We have saved the current account information into the file!").show();
                                } else {
                                    new Alert(ERROR, "An I/O error occurred...").show();
                                }
                            }
                        });
            }

            private void showIncorrectCredentialsWindow() {
                new Alert(ERROR, "You have entered an incorrect credentials!").show();
            }

        @FXML
        public void closeAuthorisationWindow(ActionEvent event) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }

    }

}
