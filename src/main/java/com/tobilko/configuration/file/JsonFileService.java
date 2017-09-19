package com.tobilko.configuration.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tobilko.data.account.Account;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class JsonFileService {

    private static final String PATH_TO_SAVED_ACCOUNT = "account.json";
    private static final String PATH_TO_ALL_ACCOUNTS = "accounts.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static boolean serialiseAccount(Account account) {
        try {
            MAPPER.writeValue(new File(PATH_TO_SAVED_ACCOUNT), account);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @SneakyThrows
    public static void serialiseAccounts(List<Account> accounts) {
        MAPPER.writeValue(new File(PATH_TO_ALL_ACCOUNTS), accounts);
    }

    @SneakyThrows
    public static List<Account> deserialiseAccounts() {
        return MAPPER.readValue(new File(PATH_TO_ALL_ACCOUNTS),
                new TypeReference<ArrayList<Account>>() {});
    }

}
