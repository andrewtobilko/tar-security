package com.tobilko.data.role;

import com.google.common.collect.ImmutableList;
import com.tobilko.data.action.Action;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

import static com.tobilko.data.action.Action.*;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public enum Role {

    ADMIN_ACCOUNT(ImmutableList.of(CREATE_ACCOUNT, REMOVE_SELECTED_ACCOUNT, EDIT_CURRENT_ACCOUNT, EDIT_SELECTED_ACCOUNT, BLOCK_ALL_ACCOUNTS, UNBLOCK_ALL_ACCOUNTS, EXIT)),
    ORDINARY_ACCOUNT(ImmutableList.of(EDIT_CURRENT_ACCOUNT, EXIT));

    @Getter
    private List<Action> abilities;

    Role(List<Action> actions) {
        abilities = actions;
    }

    public Optional<Action> getAbilityById(String id) {
        return this.abilities.stream().filter(ability -> ability.getTabId().equals(id)).findAny();
    }
}
