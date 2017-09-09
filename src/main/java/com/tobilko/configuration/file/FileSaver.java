package com.tobilko.configuration.file;

import com.tobilko.data.account.Account;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Andrew Tobilko on 9/9/17.
 */
public final class FileSaver {

    private static final String PATH_TO_FILE_TO_SAVE_IN = "account.txt";

    public static boolean saveAccountDetailsToFile(Account account) {
        try {
            Files.write(Paths.get(PATH_TO_FILE_TO_SAVE_IN), account.toFineString().getBytes());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}
