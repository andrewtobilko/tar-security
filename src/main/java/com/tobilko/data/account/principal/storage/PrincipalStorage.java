package com.tobilko.data.account.principal.storage;

import com.tobilko.data.account.principal.RolePrincipal;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
public interface PrincipalStorage {

    Optional<RolePrincipal> getPrincipal();

    void putPrincipalIntoStorage(RolePrincipal principal);

    void clearStorage();

}
