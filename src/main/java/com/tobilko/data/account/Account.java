package com.tobilko.data.account;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by Andrew Tobilko on 9/8/17.
 */
@Data
@RequiredArgsConstructor
public final class Account {

    private @NonNull String name;
    private @NonNull String password;

}
