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
    public void putPrincipalIntoStorage(RolePrincipal principal) {
        this.principal = principal;
    }

    @Override
    public void clearStorage() {
        this.principal = null;
    }

}
