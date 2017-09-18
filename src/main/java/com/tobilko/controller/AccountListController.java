package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.data.account.Account;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.role.Role;
import com.tobilko.data.storage.AccountStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.Getter;

import java.util.Optional;

import static com.tobilko.configuration.event.EventType.SELECTED_ACCOUNT_CHANGED;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public final class AccountListController extends Controller {

    private final PrincipalStorage principalStorage;
    private final AccountStorage accountStorage;

    private @Getter Account currentAccount;
    private @FXML ListView<Account> accountListView;

    @Inject
    public AccountListController(EventBus eventBus, PrincipalStorage principalStorage, AccountStorage accountStorage) {
        super(eventBus);

        this.principalStorage = principalStorage;
        this.accountStorage = accountStorage;

        eventBus.register(this);
    }

    @FXML
    public void handleSelectAction(javafx.event.Event event) {
        getEventBus().post(new Event(
                SELECTED_ACCOUNT_CHANGED,
                accountListView.getSelectionModel().getSelectedItem()
        ));
    }

    @Subscribe
    public void handleEvent(Event event) {

        switch (event.getType()) {

            case PRINCIPAL_CHANGED:
                handlePrincipalChangedEvent(event);
                break;

            case SELECTED_ACCOUNT_CHANGED:
                handleSelectedAccountChanged(event);
                break;

            case NEW_ACCOUNT_CREATED:
                handleNewAccountCreated(event);
                break;

            case ACCOUNT_LIST_CHANGED:
                handleAccountListChanged(event);
                break;

            default:
                // skip other types
        }

    }

    private void handlePrincipalChangedEvent(Event event) {
        final Optional<RolePrincipal> optionalPrincipal = principalStorage.getPrincipal();

        if (!optionalPrincipal.isPresent()) {
            accountListView.setVisible(false);

            return;
        }

        final RolePrincipal principal = optionalPrincipal.get();
        final boolean isPrincipalAdmin = principal.getRole().equals(Role.ADMIN_ACCOUNT);

        accountListView.setVisible(isPrincipalAdmin);

        if (isPrincipalAdmin) {
            fillUpAccountListViewExcludingPrincipal(principal);
        }
    }

    private void fillUpAccountListViewExcludingPrincipal(RolePrincipal principal) {
        final ObservableList<Account> list = FXCollections.observableArrayList();

        for (Account account : accountStorage.getAll()) {
            if (!principal.getName().equals(account.getName())) {
                list.add(account);
            }
        }

        accountListView.setItems(list);
    }

    private void handleSelectedAccountChanged(Event event) {
        Object payload = event.getPayload();

        if (payload instanceof Account) {
            accountStorage.getAccountByName(((Account) payload).getName()).ifPresent(account -> {
                this.currentAccount = account;
            });
        }
    }

    private void handleNewAccountCreated(Event event) {
        final Account payload = (Account) event.getPayload();

        accountStorage.saveAccount(payload);
        accountListView.getItems().add(payload);
    }

    private void handleAccountListChanged(Event event) {
        Account payload = (Account) event.getPayload();

        // can't handle this case
        if (!(event.getPayload() instanceof Account)) {
            return;
        }

        String payloadAccountName = ((Account) event.getPayload()).getName();
        accountListView.getItems().removeIf(account -> account.getName().equals(payloadAccountName));
    }

}
