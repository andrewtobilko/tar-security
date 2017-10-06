package com.tobilko.lab1.data.account;

import com.tobilko.lab1.data.account.principal.RolePrincipal;
import com.tobilko.lab1.data.role.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public final class Account implements RolePrincipal {

    private @NonNull String name;
    private @NonNull String password;
    private @NonNull Role role;
    private @NonNull boolean blocked;

    public boolean equals(Object that) {
        return this == that ||
                that instanceof Account && ((Account) that).getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role);
    }

    public String toString() {
        return "[name=" + name + ",role=" + role + "]";
    }

    public String toFineString() {
        return "Name: " + (name == null ? "Unknown" : name) +
                "\nRole: " + role.name() +
                "\nAbilities: " + role.getAbilities();
    }

}
