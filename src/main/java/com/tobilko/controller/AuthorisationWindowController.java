package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.file.JsonFileService;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.storage.AccountStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.tobilko.configuration.event.EventType.PRINCIPAL_CHANGED;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Andrew Tobilko on 9/18/17.
 */
@Singleton
public class AuthorisationWindowController extends Controller {

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
                        if (JsonFileService.serialiseAccount(account)) {
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