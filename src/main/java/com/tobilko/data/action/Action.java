package com.tobilko.data.action;

import com.google.inject.Injector;
import com.tobilko.data.action.consumer.InjectorConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static com.tobilko.data.action.title.ActionTitle.*;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@RequiredArgsConstructor
public enum Action {

    CREATE_ACCOUNT(
            CREATE_ACCOUNT_ACTION_TITLE,
            InjectorConsumer::createAccount
    ),

    REMOVE_SELECTED_ACCOUNT(
            REMOVE_SELECTED_ACCOUNT_ACTION_TITLE,
            InjectorConsumer::removeSelectedAccount
    ),

    EDIT_CURRENT_ACCOUNT(
            EDIT_CURRENT_ACCOUNT_ACTION_TITLE,
            InjectorConsumer::editCurrentAccount
    ),

    EDIT_SELECTED_ACCOUNT(
            EDIT_SELECTED_ACCOUNT_ACTION_TITLE,
            InjectorConsumer::editSelectedAccount
    ),

    BLOCK_ALL_ACCOUNTS(
            BLOCK_ALL_ACCOUNTS_ACTION_TITLE,
            InjectorConsumer::blockAllAccountsConsumer
    ),

    UNBLOCK_ALL_ACCOUNTS(
            UNBLOCK_ALL_ACCOUNTS_ACTION_TITLE,
            InjectorConsumer::unblockAllAccountsConsumer
    ),

    EXIT(
            EXIT_ACTION_TITLE,
            InjectorConsumer::exit
    );

    @Getter
    private final String tabId;

    private final Consumer<Injector> actionPerformer;

    public void performAction(Injector injector) {
        actionPerformer.accept(injector);
    }
}
