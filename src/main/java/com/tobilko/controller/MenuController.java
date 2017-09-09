package com.tobilko.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tobilko.configuration.event.Event;
import com.tobilko.configuration.event.EventType;
import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.account.principal.storage.PrincipalStorage;
import com.tobilko.data.action.Action;
import com.tobilko.data.role.Role;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Andrew Tobilko on 9/7/17.
 */
@Singleton
public final class MenuController implements Controller {

    private final EventBus eventBus;
    private final Injector injector;
    private final PrincipalStorage principalStorage;

    @FXML
    private MenuBar menuBar;

    @Inject
    public MenuController(
            EventBus eventBus,
            PrincipalStorage principalStorage,
            Injector injector
    ) {
        this.eventBus = eventBus;
        this.injector = injector;
        this.principalStorage = principalStorage;

        eventBus.register(this);
    }

    public void handleDeveloperInformationButtonClick(ActionEvent event) {
        new Alert(
                Alert.AlertType.INFORMATION,
                DeveloperInformation.DEVELOPED_BY_TITLE
        ).show();

    }

    private class DeveloperInformation {

        private static final String DEVELOPED_BY_TITLE = "Designed by Andrew Tobilko, 2017";

    }

    @Subscribe
    public void handleEvent(Event event) {

        if (event.getType().equals(EventType.PRINCIPAL_CHANGED)) {

            Optional<RolePrincipal> principal = principalStorage.getPrincipal();

            if (principal.isPresent()) {

                menuBar.setDisable(false);
                adaptMenuItemsForPrincipal(principal.get());

            } else {
                menuBar.setDisable(true);
            }

        }

    }

    private void adaptMenuItemsForPrincipal(RolePrincipal principal) {
        Role role = principal.getRole();

        Set<String> availableOptions = role.getAbilities().stream().map(Action::getTabId).collect(Collectors.toSet());

        for (Menu menu : menuBar.getMenus()) {
            if (menu.getId() != null && menu.getId().equals("actions")) {
                for (MenuItem menuItem : menu.getItems()) {
                    menuItem.setVisible(availableOptions.contains(menuItem.getId()));
                }
            }
        }
    }

    public void handleMenuItemSelected(javafx.event.Event event) {
        EventTarget target = event.getTarget();


        if (target instanceof MenuItem) {
            MenuItem item = (MenuItem)target;

            principalStorage.getPrincipal().ifPresent(principal -> {

                principal.getRole().getAbilityById(item.getId()).ifPresent(ability -> {

                    ability.performAction(injector, menuBar);

                });
            });
        }
    }

}
