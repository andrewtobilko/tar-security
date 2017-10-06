package com.tobilko.lab1.data.action.consumer;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.tobilko.lab1.configuration.event.Event;
import com.tobilko.lab1.configuration.event.EventType;
import com.tobilko.lab1.controller.AccountListController;
import com.tobilko.lab1.controller.ApplicationController;
import com.tobilko.lab1.data.account.Account;
import com.tobilko.lab1.data.account.principal.RolePrincipal;
import com.tobilko.lab1.data.account.principal.storage.PrincipalStorage;
import com.tobilko.lab1.data.role.Role;
import com.tobilko.lab1.data.storage.AccountStorage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

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
            new Alert(Alert.AlertType.ERROR, "You haven't picked up any account...").show();
            return;
        }

        changePasswordForAccount(injector, selectedAccount);
    }

    private static void changePasswordForAccount(Injector injector, Account account) {

        final TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setContentText("Enter a new password to change:");

        passwordDialog.showAndWait().ifPresent(account::setPassword);

        final AccountStorage storage = injector.getInstance(AccountStorage.class);
        storage.updateAccount(account);
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
        passwordDialog.showAndWait().ifPresent(account::setPassword);

        // notify about a new account created
        final EventBus eventBus = injector.getInstance(EventBus.class);
        eventBus.post(new Event(EventType.NEW_ACCOUNT_CREATED, account));
    }

    public static void removeSelectedAccount(Injector injector) {
        final AccountListController controller = injector.getInstance(AccountListController.class);

        Account selectedAccount = controller.getCurrentAccount();

        if (selectedAccount == null) {
            new Alert(Alert.AlertType.ERROR, "You haven't picked up any account...").show();
            return;
        }

        new Alert(Alert.AlertType.CONFIRMATION, "You wanna remove the account. Are you sure?")
                .showAndWait()
                .ifPresent(buttonType -> {
                            if (!buttonType.getButtonData().isCancelButton()) {
                                final AccountStorage accountStorage = injector.getInstance(AccountStorage.class);
                                accountStorage.removeAccount(selectedAccount);

                                // notify about the account removed
                                final EventBus eventBus = injector.getInstance(EventBus.class);
                                eventBus.post(new Event(EventType.ACCOUNT_LIST_CHANGED, selectedAccount));
                            }
                        }
                );
    }

    public static void editCurrentAccount(Injector injector) {
        final PrincipalStorage instance = injector.getInstance(PrincipalStorage.class);

        instance.getPrincipal().ifPresent(principal -> {
            if (principal instanceof Account) {
                changePasswordForAccount(injector, (Account) principal);
            }
        });
    }

    private InjectorConsumer() {}

}
