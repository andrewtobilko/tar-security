package com.tobilko.data.account;

import com.tobilko.data.account.principal.RolePrincipal;
import com.tobilko.data.role.Role;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Data
@RequiredArgsConstructor
public final class Account implements RolePrincipal {

    private @NonNull String name;
    private @NonNull String password;
    private @NonNull Role role;

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

}
