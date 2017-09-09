package com.tobilko.data.action;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.controller.AccountListController;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.BiConsumer;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor
public enum Action {

    CREATE_ACCOUNT("createAccount", (injector, node) -> {

        final Account account = new Account();
        account.setRole(Role.ORDINARY_ACCOUNT);

        EventBus eventBus = injector.getInstance(EventBus.class);
        PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);


        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setContentText("Enter the account name:");

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setContentText("Enter the account password:");

        nameDialog.showAndWait().ifPresent(account::setName);
        passwordDialog.showAndWait().ifPresent(s -> account.setPassword(encoder.encode(s)));

        eventBus.post(new Event(EventType.NEW_ACCOUNT_CREATED, account));

    }),
    REMOVE_SELECTED_ACCOUNT("removeAccount", (Injector injector, Node node) -> {
        AccountListController controller = injector.getInstance(AccountListController.class);
        AccountStorage accountStorage = injector.getInstance(AccountStorage.class);

        Account currentAccount = controller.getCurrentAccount();
        new Alert(Alert.AlertType.CONFIRMATION, "You wanna remove the account. Are you sure?").showAndWait().ifPresent(
                result -> {
                    if (!result.getButtonData().isCancelButton()) {
                        accountStorage.removeAccount(currentAccount);
                    }
                }
        );
    }),
    EDIT_CURRENT_ACCOUNT("editAccount", (injector, node) -> {
        PrincipalStorage instance = injector.getInstance(PrincipalStorage.class);
        PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);

        instance.getPrincipal().ifPresent(principal -> {
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setContentText("Enter a new password to change:");

            passwordDialog.showAndWait().ifPresent(value -> {
                principal.setPassword(encoder.encode(value));
            });
        });
    }),
    EDIT_SELECTED_ACCOUNT("editSelectedAccount", (injector, node) -> {
        AccountListController controller = injector.getInstance(AccountListController.class);

        Account currentAccount = controller.getCurrentAccount();

        if (currentAccount == null) {
            new Alert(Alert.AlertType.ERROR, "You didn't choose any account...");
        } else {
            AccountStorage storage = injector.getInstance(AccountStorage.class);
            PasswordEncoder encoder = injector.getInstance(PasswordEncoder.class);

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setContentText("Enter a new password to change:");

            passwordDialog.showAndWait().ifPresent(value -> {
                currentAccount.setPassword(encoder.encode(value));
            });

            storage.updateAccount(currentAccount);
        }
    }),
    BLOCK_ALL_ACCOUNTS("blockAllAccounts", (injector, node)-> {

        AccountStorage storage = injector.getInstance(AccountStorage.class);
        PrincipalStorage principalStorage = injector.getInstance(PrincipalStorage.class);

        RolePrincipal principal = principalStorage.getPrincipal().orElseThrow(() -> new IllegalArgumentException("no principal"));

        for (Account account : storage.getAll()) {
            if (!account.getName().equals(principal.getName())) {
                account.setBlocked(true);
            }
        }

        new Alert(Alert.AlertType.INFORMATION, "All the accounts have been blocked!").show();

    }),
    UNBLOCK_ALL_ACCOUNTS("unblockAllAccounts", (injector, node)-> {
        AccountStorage storage = injector.getInstance(AccountStorage.class);

        for (Account account : storage.getAll()) {
                account.setBlocked(false);
        }

        new Alert(Alert.AlertType.INFORMATION, "All the accounts have been unblocked!").show();
    }),
    EXIT("exit", (injector, node)-> {
        node.getScene().getWindow().hide();
    });

    @Getter
    private final String tabId;

    private final BiConsumer<Injector, Node> consumer;

    public void performAction(Injector injector, Node node) {
        consumer.accept(injector, node);
    }
}
