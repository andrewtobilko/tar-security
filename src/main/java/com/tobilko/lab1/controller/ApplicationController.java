package com.tobilko.lab1.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.lab1.configuration.event.Event;
import com.tobilko.lab1.configuration.event.EventType;
import com.tobilko.lab1.data.account.principal.RolePrincipal;
import com.tobilko.lab1.data.account.principal.storage.PrincipalStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
public final class ApplicationController extends Controller {

    private final PrincipalStorage principalStorage;

    @FXML
    @Getter
    private GridPane applicationGridPane;

    private @FXML Label currentAccountNameLabel;
    private @FXML VBox currentAccountInformation;

    @Inject
    public ApplicationController(
            EventBus eventBus,
            PrincipalStorage principalStorage
    ) {
        super(eventBus);
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(Event event) {
        if (!event.getType().equals(EventType.PRINCIPAL_CHANGED)) {
            return;
        }

        final Optional<RolePrincipal> optionalPrincipal = principalStorage.getPrincipal();
        final boolean isPrincipalPresent = optionalPrincipal.isPresent();

        currentAccountInformation.setVisible(isPrincipalPresent);

        if (isPrincipalPresent) {
            currentAccountNameLabel.setText(generateAccountTitleByPrincipal(optionalPrincipal.get()));
        }
    }

    private  String generateAccountTitleByPrincipal(RolePrincipal principal) {
        return principal.getName() + ", " + principal.getRole().name();
    }

}
