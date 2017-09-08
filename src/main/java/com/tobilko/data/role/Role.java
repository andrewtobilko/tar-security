package com.tobilko.data.role;

import com.tobilko.data.action.Action;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public enum Role {

    ADMIN(new ArrayList<>()),
    ORDINARY_USER(new ArrayList<>());

    @Getter
    private List<Action> abilities;

    Role(List<Action> actions) {
        abilities = actions;
    }

    public boolean canPerformAction(Action action) {
        return abilities.stream().anyMatch(a -> a.equals(action));
    }

}
