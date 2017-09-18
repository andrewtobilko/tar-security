package com.tobilko.data.action.consumer;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.controller.AccountListController;
import com.tobilko.controller.ApplicationController;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/10/17.
 */
public final class InjectorConsumer {

    public static void blockAllAccountsConsumer(Injector injector) {
        final RolePrincipal principal = injector.getInstance(PrincipalStorage.class)
                .getPrincipal()
                .orElseThrow(() -> new IllegalArgumentException("no principal"));

        final AccountStorage storage = injector.getInstance(AccountStorage.class);
        final List<Account> accounts = storage.getAll();

        if (accounts.isEmpty()) {
            return;
        }

        for (Account account : accounts) {
            if (!account.getName().equals(principal.getName())) {
                account.setBlocked(true);
            }
        }
        showBlockingStatusChangingAlert(true);
    }

    public static void unblockAllAccountsConsumer(Injector injector) {
        final AccountStorage storage = injector.getInstance(AccountStorage.class);
        final List<Account> accounts = storage.getAll();

        if (accounts.isEmpty()) {
            return;
        }

        for (Account account : accounts) {
            account.setBlocked(false);
        }
        showBlockingStatusChangingAlert(false);
    }

    private static void showBlockingStatusChangingAlert(boolean blockingStatus) {
        new Alert(
                Alert.AlertType.INFORMATION,
                "All the accounts' blocking statuses have been set to " + blockingStatus + "!"
        ).show();
    }

    public static void exit(Injector injector) {
        injector.getInstance(ApplicationController.class)
                .getApplicationGridPane()
                .getScene()
                .getWindow()
                .hide();
    }

    public static void editSelectedAccount(Injector injector) {

        Account selectedAccount = injector.getInstance(AccountListController.class).getCurrentAccount();

        if (selectedAccount == null) {
            new Alert(Alert.AlertType.ERROR, "You haven't picked up any account...");
            return;
        }

        final TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setContentText("Enter a new password to change:");

        passwordDialog.showAndWait().ifPresent(newPassword -> {
            final PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);
            selectedAccount.setPassword(encoder.encode(newPassword));
        });

        final AccountStorage storage = injector.getInstance(AccountStorage.class);
        storage.updateAccount(selectedAccount);
    }

    public static void createAccount(Injector injector) {
        final Account account = new Account();
        account.setRole(Role.ORDINARY_ACCOUNT);

        // name dialog
        final TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setContentText("Enter the account name:");

        // password dialog
        final TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setContentText("Enter the account password:");

        // handle callbacks
        nameDialog.showAndWait().ifPresent(account::setName);
        passwordDialog.showAndWait().ifPresent(value -> {
            final PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);
            account.setPassword(encoder.encode(value));
        });

        // notify about a new account created
        final EventBus eventBus = injector.getInstance(EventBus.class);
        eventBus.post(new Event(EventType.NEW_ACCOUNT_CREATED, account));
    }

    public static void removeSelectedAccount(Injector injector) {
        final AccountListController controller = injector.getInstance(AccountListController.class);

        Account currentAccount = controller.getCurrentAccount();

        new Alert(Alert.AlertType.CONFIRMATION, "You wanna remove the account. Are you sure?")
                .showAndWait()
                .ifPresent(buttonType -> {
                            if (!buttonType.getButtonData().isCancelButton()) {
                                final AccountStorage accountStorage = injector.getInstance(AccountStorage.class);
                                accountStorage.removeAccount(currentAccount);
                            }
                        }
                );
    }

    public static void editCurrentAccount(Injector injector) {
        final PrincipalStorage instance = injector.getInstance(PrincipalStorage.class);

        instance.getPrincipal().ifPresent(principal -> {
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setContentText("Enter a new password to change:");

            passwordDialog.showAndWait().ifPresent(value -> {
                final PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);
                principal.setPassword(encoder.encode(value));
            });
        });
    }

    private InjectorConsumer() {}

}
