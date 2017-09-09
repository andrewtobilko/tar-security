package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public final class ApplicationController implements Controller {

    private final EventBus eventBus;
    private final PrincipalStorage principalStorage;

    @FXML
    private Label currentAccountNameLabel;
    @FXML
    private VBox currentAccountInformation;

    @Inject
    public ApplicationController(
            EventBus eventBus,
            PrincipalStorage principalStorage
    ) {
        this.eventBus = eventBus;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @Subscribe
    public void handleIncomingEvent(Event event) {
        if (event.getType().equals(EventType.PRINCIPAL_CHANGED)) {

            Optional<RolePrincipal> optionalPrincipal = principalStorage.getPrincipal();

            if (optionalPrincipal.isPresent()) {
                currentAccountInformation.setVisible(true);

                RolePrincipal principal = optionalPrincipal.get();
                currentAccountNameLabel.setText(principal.getName() + ", " + principal.getRole().name());
            } else {
                currentAccountInformation.setVisible(false);

            }
        }
    }

}
