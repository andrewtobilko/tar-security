package com.tobilko.lab1.data.account.principal;

import com.tobilko.lab1.data.role.Role;

import java.security.Principal;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public interface RolePrincipal extends Principal {

    Role getRole();

    void setPassword(String value);
    String getPassword();

    boolean isBlocked();
    void setBlocked(boolean value);

}
