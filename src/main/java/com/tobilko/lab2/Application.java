package com.tobilko.lab2;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew Tobilko on 9/23/17.
 */
public final class Application {

    private static final String[] KEY_STORES = {"KeychainStore"};

    private void fetchAllCertificatesFromDefaultKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        for (String storeName : KEY_STORES) {

            System.out.println("Parsing '" + storeName + "' store ...");

            KeyStore store = KeyStore.getInstance(storeName);
            store.load(null);

            for (String alias : Collections.list(store.aliases())) {

                if (store.isCertificateEntry(alias)) {
                    handleCertificateEntry(store, alias);
                } else if (store.isKeyEntry(alias)) {
                    handleKeyEntry(store, alias);
                } else {
                    handleUnknownAliasState(store, alias);
                }

            }

        }

    }

    private void handleCertificateEntry(KeyStore store, String alias) throws KeyStoreException {
        System.out.println("certificate = " + alias);

        Certificate certificate = store.getCertificate(alias);
        System.out.println("certificate type = " + certificate.getType());

        System.out.printf("\n\n");
    }

    private void handleKeyEntry(KeyStore store, String alias) {
        System.out.println("key = " + alias);
        System.out.printf("\n\n");
    }

    private void handleUnknownAliasState(KeyStore store, String alias) {
        System.out.println("??? = " + alias);
        System.out.printf("\n\n");
    }

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        new Application().fetchAllCertificatesFromDefaultKeyStore();
    }

}
