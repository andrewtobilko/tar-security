package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;

import static com.tobilko.configuration.event.EventType.NEW_ACCOUNT_CREATED;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public final class AccountListController {

    private final EventBus eventBus;
    private final PrincipalStorage principalStorage;
    private final AccountStorage accountStorage;

    @Getter
    private Account currentAccount;

    @Inject
    public AccountListController(
            EventBus eventBus,
            PrincipalStorage principalStorage,
            AccountStorage accountStorage
    ) {
        this.eventBus = eventBus;
        this.principalStorage = principalStorage;
        this.accountStorage = accountStorage;

        eventBus.register(this);
    }

    @FXML
    private ListView<Label> accountListView;

    @FXML
    public void handleSelectAction(javafx.event.Event event) {
        eventBus.post(new Event(
                EventType.SELECTED_ACCOUNT_CHANGED,
                accountListView.getSelectionModel().getSelectedItem().getText())
        );
    }

    @Subscribe
    public void handlePrincipalChangedAction(Event event) { // todo : event type

        EventType type = event.getType();

        if (type.equals(EventType.PRINCIPAL_CHANGED)) {

            Optional<RolePrincipal> optionalPrincipal = principalStorage.getPrincipal();

            if (optionalPrincipal.isPresent() && optionalPrincipal.get().getRole().equals(Role.ADMIN_ACCOUNT)) {
                accountListView.setVisible(true);
                ObservableList<Label> labels = FXCollections.observableArrayList();


                for (Account account : accountStorage.getAll()) {
                    if (!optionalPrincipal.get().getName().equals(account.getName())) {
                        labels.add(new Label(account.getName() + ", " + account.getRole().name()));
                    }
                }
                accountListView.setItems(labels);
            } else {
                accountListView.setVisible(false);
            }
            // todo
        } else if (type.equals(EventType.SELECTED_ACCOUNT_CHANGED)) {
            accountStorage.getAccountByName(event.getPayload().toString().split(",")[0]).ifPresent(account -> {
                System.out.println("acccount = " + account);
                this.currentAccount = account;
            });
        } else if (type.equals(NEW_ACCOUNT_CREATED)) {
            Account payload = (Account) event.getPayload();


            accountStorage.saveAccount(payload);

            HashSet<Label> labels = new HashSet<>(accountListView.getItems());
            labels.add(new Label(payload.getName() + ", " + payload.getRole()) );

            accountListView.setItems(convertSetToObservableList(labels));
        } else if (type.equals(EventType.ACCOUNT_LIST_CHANGED)) {
            Account payload = (Account) event.getPayload();

            accountListView.getItems().removeIf(l -> l.getText().equals(payload.getName() + ", " + payload.getRole()));
        }

    }

    private ObservableList<Label> convertSetToObservableList(HashSet<Label> labels) {
        return FXCollections.observableArrayList(labels);
    }

}
