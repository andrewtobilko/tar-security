package com.tobilko.data.role;

import com.google.common.collect.ImmutableList;
import com.tobilko.data.action.Action;
import lombok.Getter;

import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public enum Role {

    ADMIN(ImmutableList.of()),
    ORDINARY_USER(ImmutableList.of());

    @Getter
    private List<Action> abilities;

    Role(List<Action> actions) {
        abilities = actions;
    }

    public boolean canPerformAction(Action action) {
        return abilities.stream().anyMatch(a -> a.equals(action));
    }

}
