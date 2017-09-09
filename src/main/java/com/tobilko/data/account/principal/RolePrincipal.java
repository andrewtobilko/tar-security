package com.tobilko.data.account.principal;

import com.tobilko.data.role.Role;

import java.security.Principal;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public interface RolePrincipal extends Principal {

    Role getRole();

    void setPassword(String value);
    String getPassword();

    boolean isBlocked();
    void setBlockedStatus(boolean value);

}
