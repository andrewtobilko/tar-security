package com.tobilko.lab2.certificate;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew Tobilko on 10/8/17.
 */
public final class CertificateRepository {

    private final String[] KEY_STORES = {"KeychainStore"};

    @Getter
    private final List<X509Certificate> certificates;

    @SneakyThrows
    public CertificateRepository() {
        certificates = new ArrayList<>();
        fetchAllCertificatesFromDefaultKeyStore();
    }

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

        if ("X.509".equals(certificate.getType())) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            System.out.println("issuer name = " + x509Certificate.getIssuerDN().getName());

            System.out.println("alg name = " + x509Certificate.getSigAlgName());
            System.out.println("sig name = " + x509Certificate.getSigAlgOID());

            certificates.add(x509Certificate);
        }

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

}
