package com.tobilko.lab1.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tobilko.lab1.configuration.constant.DeveloperInformation;
import com.tobilko.lab1.configuration.event.Event;
import com.tobilko.lab1.configuration.event.EventType;
import com.tobilko.lab1.data.account.principal.RolePrincipal;
import com.tobilko.lab1.data.account.principal.storage.PrincipalStorage;
import com.tobilko.lab1.data.action.Action;
import com.tobilko.lab1.data.role.Role;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Andrew Tobilko on 9/7/17.
 */
@Singleton
public final class MenuController extends Controller {

    private final Injector injector;
    private final PrincipalStorage principalStorage;

    private @FXML MenuBar menuBar;

    @Inject
    public MenuController(
            EventBus eventBus,
            PrincipalStorage principalStorage,
            Injector injector
    ) {
        super(eventBus);

        this.injector = injector;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    @FXML
    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        new Alert(
                Alert.AlertType.INFORMATION,
                DeveloperInformation.DEVELOPED_BY_TITLE
        ).show();
    }

    @Subscribe
    public void handleEvent(Event event) {

        if (event.getType().equals(EventType.PRINCIPAL_CHANGED)) {

            final Optional<RolePrincipal> principal = principalStorage.getPrincipal();
            final boolean isPrincipalPresent = principal.isPresent();

            menuBar.setDisable(!isPrincipalPresent);

            if (isPrincipalPresent) {
                adaptMenuItemsForPrincipal(principal.get());
            }

        }

    }

        private void adaptMenuItemsForPrincipal(RolePrincipal principal) {
            final Role role = principal.getRole();

            final Set<String> availableOptions = role.getAbilities().stream().map(Action::getTabId).collect(toSet());
            final String ACTIONS_SUBMENU_IDENTIFIER = "actions";

            menuBar.getMenus()
                    .stream()
                    .filter(menu -> menu.getId() != null && menu.getId().equals(ACTIONS_SUBMENU_IDENTIFIER))
                    .flatMap(menu -> menu.getItems().stream())
                    .forEach(item -> item.setVisible(availableOptions.contains(item.getId())));
        }

    public void handleMenuItemSelected(javafx.event.Event event) {
        EventTarget target = event.getTarget();

        if (!(target instanceof MenuItem)) {
            return;
        }

        final MenuItem item = (MenuItem) target;

        principalStorage.getPrincipal()
                .ifPresent(principal ->
                        principal.getRole().getAbilityById(item.getId())
                                .ifPresent(ability -> ability.performAction(injector)));
    }

}
