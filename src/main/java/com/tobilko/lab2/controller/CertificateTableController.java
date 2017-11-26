package com.tobilko.lab2.controller;

import com.tobilko.lab2.certificate.CertificateRepository;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

/**
 * Created by Andrew Tobilko on 10/8/17.
 */
public final class CertificateTableController implements Initializable {

    @FXML
    private TableView<List<String>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CertificateRepository repository = new CertificateRepository();
        List<X509Certificate> certificates = repository.getCertificates();

        ColumnDefinition[] columnDefinitions = {
                ColumnDefinition.of("Issuer Country", 50),
                ColumnDefinition.of("Issuer State", 150),
                ColumnDefinition.of("Issuer Locality", 70),
                ColumnDefinition.of("Issuer Organization", 150),
                ColumnDefinition.of("Issuer Common Name", 150),

                ColumnDefinition.of("Algorithm", 130),
                ColumnDefinition.of("Signature", 150),
                ColumnDefinition.of("Serial number", 150),
                ColumnDefinition.of("Version", 50),
                ColumnDefinition.of("Not valid before", 250),
                ColumnDefinition.of("Not valid after", 250),

                ColumnDefinition.of("Public key algorithm", 70),
                ColumnDefinition.of("Public key format", 70)
        };

        for (int i = 0; i < columnDefinitions.length; i++) {
            ColumnDefinition columnDefinition = columnDefinitions[i];
            TableColumn<List<String>, String> column = new TableColumn<>(columnDefinition.getTitle());
            column.setMinWidth(columnDefinition.getWidth());
            column.setPrefWidth(columnDefinition.getWidth());

            final int columnIndex = i;
            column.setCellValueFactory(data -> {
                List<String> values = data.getValue();
                return new ReadOnlyStringWrapper(columnIndex < values.size() ? values.get(columnIndex) : "");
            });

            table.getColumns().add(column);
        }

        ObservableList<List<String>> items = table.getItems();

        for (X509Certificate certificate : certificates) {

            Map<String, String> issuerData = parseIssuerInformation(certificate.getIssuerDN().getName());
            PublicKey key = certificate.getPublicKey();

            items.add(asList(
                    issuerData.getOrDefault("C", "-"),
                    issuerData.getOrDefault("ST", "-"),
                    issuerData.getOrDefault("L", "-"),
                    issuerData.getOrDefault("O", "-"),
                    issuerData.getOrDefault("CN", "-"),

                    certificate.getSigAlgName(),
                    certificate.getSigAlgOID(),
                    certificate.getSerialNumber().toString(),
                    String.valueOf(certificate.getVersion()),
                    certificate.getNotBefore().toString(),
                    certificate.getNotAfter().toString(),

                    key.getAlgorithm(),
                    key.getFormat()
            ));
        }
    }

    private Map<String, String> parseIssuerInformation(String information) {
        return stream(information.split(", "))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    private static class ColumnDefinition {
        private final String title;
        private final double width;
    }


}
