package com.tobilko.data.account.principal.storage;

import com.tobilko.data.account.principal.RolePrincipal;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public final class PrincipalStorageProvider implements PrincipalStorage {

    private RolePrincipal principal;

    @Override
    public Optional<RolePrincipal> getPrincipal() {
        return Optional.ofNullable(principal);
    }

    @Override
    public boolean putPrincipalIntoStorage(RolePrincipal principal) {
        if (principal.getName() == null || principal.getName().equals(this.principal.getName())) {
            return false;
        }

        this.principal = principal;

        return true;
    }

    @Override
    public boolean clearStorage() {
        if (principal == null) {
            return false;
        }

        this.principal = null;

        return true;
    }

}
