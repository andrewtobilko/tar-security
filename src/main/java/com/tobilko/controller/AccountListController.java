package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.role.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.Observable;
import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public final class AccountListController {

    private final EventBus eventBus;
    private final PrincipalStorage principalStorage;

    @Inject
    public AccountListController(
            EventBus eventBus,
            PrincipalStorage principalStorage
    ) {
        this.eventBus = eventBus;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @FXML
    private ListView<Label> accountListView;

    @FXML // todo : rename
    public void handleSelectAction(MouseEvent event) {
        System.out.println(event.getEventType());
        eventBus.post(new Event(EventType.SELECTED_ACCOUNT_CHANGED, null)); // todo
    }

    @Subscribe
    public void handlePrincipalChangedAction(Event event) {

        if (event.getType().equals(EventType.PRINCIPAL_CHANGED)) {

            Optional<RolePrincipal> optionalPrincipal = principalStorage.getPrincipal();

            if (optionalPrincipal.isPresent() && optionalPrincipal.get().getRole().equals(Role.ADMIN)) {
                accountListView.setVisible(true);
                ObservableList<Label> labels = FXCollections.observableArrayList();

                labels.addAll(
                        new Label("1"),
                        new Label("2")
                );

                accountListView.setItems(labels);
            } else {
                accountListView.setVisible(false);
            }
        }

    }

}
