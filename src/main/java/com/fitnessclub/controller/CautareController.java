package com.fitnessclub.controller;

import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Client;
import com.fitnessclub.service.AbonamentService;
import com.fitnessclub.service.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CautareController {

    @FXML private TextField txtNumeClient, txtEmailClient;
    @FXML private ComboBox<String> cmbAbonamentActiv;
    @FXML private DatePicker dpNascutDupa;
    @FXML private TableView<Client> tabelClienti;
    @FXML private TableColumn<Client, String> colCId, colCNume, colCPrenume,
            colCEmail, colCTelefon, colCActiv;
    @FXML private Label lblRezultateClienti;

    @FXML private ComboBox<TipAbonament> cmbTipAb;
    @FXML private ComboBox<StatusAbonament> cmbStatusAb;
    @FXML private TextField txtPretMin, txtPretMax;
    @FXML private DatePicker dpExpiraDupa, dpExpinaInainte;
    @FXML private TableView<Abonament> tabelAbonamente;
    @FXML private TableColumn<Abonament, String> colAId, colAClient, colATip,
            colAStatus, colAExpirare, colAPret, colAZile;
    @FXML private Label lblRezultateAbonamente;

    private final ClientService clientService       = new ClientService();
    private final AbonamentService abonamentService = new AbonamentService();

    @FXML
    public void initialize() {
        initColoanClienti();
        initColoanAbonamente();

        cmbAbonamentActiv.setItems(FXCollections.observableArrayList(
                "Toate", "Cu abonament activ", "Fără abonament activ"));
        cmbAbonamentActiv.setValue("Toate");

        cmbTipAb.setItems(FXCollections.observableArrayList(TipAbonament.values()));
        cmbStatusAb.setItems(FXCollections.observableArrayList(StatusAbonament.values()));

        tabelClienti.setItems(FXCollections.observableArrayList(
                clientService.totiClientii()));
        tabelAbonamente.setItems(FXCollections.observableArrayList(
                abonamentService.toateAbonamentele()));

        lblRezultateClienti.setText(
                "Total: " + clientService.totiClientii().size() + " clienți");
        lblRezultateAbonamente.setText(
                "Total: " + abonamentService.toateAbonamentele().size() + " abonamente");
    }


    private void initColoanClienti() {
        colCId.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getId())));
        colCNume.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNume()));
        colCPrenume.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPrenume()));
        colCEmail.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEmail()));
        colCTelefon.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTelefon()));
        colCActiv.setCellValueFactory(d -> new SimpleStringProperty(
                clientService.areAbonamentActiv(d.getValue().getId())
                        ? "Activ" : "Inactiv"));
    }

    private void initColoanAbonamente() {
        colAId.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getId())));
        colAClient.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getClient().getPrenume() + " "
                        + d.getValue().getClient().getNume()));
        colATip.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTip().name()));
        colAStatus.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getStatus().name()));
        colAExpirare.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataExpirare().toString()));
        colAPret.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f lei", d.getValue().getPret())));
        colAZile.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().esteActiv()
                        ? d.getValue().zileRamase() + " zile"
                        : "Expirat"));
    }


    @FXML
    public void cautaClienti() {
        List<Client> rezultate = clientService.totiClientii();

        String nume = txtNumeClient.getText().trim();
        if (!nume.isEmpty()) {
            rezultate = rezultate.stream()
                    .filter(c -> c.getNume().toLowerCase()
                            .contains(nume.toLowerCase())
                            || c.getPrenume().toLowerCase()
                            .contains(nume.toLowerCase()))
                    .collect(Collectors.toList());
        }

        String email = txtEmailClient.getText().trim();
        if (!email.isEmpty()) {
            rezultate = rezultate.stream()
                    .filter(c -> c.getEmail().toLowerCase()
                            .contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }

        String filtruActiv = cmbAbonamentActiv.getValue();
        if ("Cu abonament activ".equals(filtruActiv)) {
            rezultate = rezultate.stream()
                    .filter(c -> clientService.areAbonamentActiv(c.getId()))
                    .collect(Collectors.toList());
        } else if ("Fără abonament activ".equals(filtruActiv)) {
            rezultate = rezultate.stream()
                    .filter(c -> !clientService.areAbonamentActiv(c.getId()))
                    .collect(Collectors.toList());
        }

        LocalDate nascutDupa = dpNascutDupa.getValue();
        if (nascutDupa != null) {
            rezultate = rezultate.stream()
                    .filter(c -> c.getDataNasterii().isAfter(nascutDupa))
                    .collect(Collectors.toList());
        }

        tabelClienti.setItems(FXCollections.observableArrayList(rezultate));
        lblRezultateClienti.setText("Găsiți: " + rezultate.size() + " clienți");
    }

    @FXML
    public void resetaClienti() {
        txtNumeClient.clear();
        txtEmailClient.clear();
        cmbAbonamentActiv.setValue("Toate");
        dpNascutDupa.setValue(null);
        List<Client> toti = clientService.totiClientii();
        tabelClienti.setItems(FXCollections.observableArrayList(toti));
        lblRezultateClienti.setText("Total: " + toti.size() + " clienți");
    }


    @FXML
    public void cautaAbonamente() {
        List<Abonament> rezultate = abonamentService.toateAbonamentele();

        TipAbonament tip = cmbTipAb.getValue();
        if (tip != null) {
            rezultate = rezultate.stream()
                    .filter(a -> a.getTip() == tip)
                    .collect(Collectors.toList());
        }

        StatusAbonament status = cmbStatusAb.getValue();
        if (status != null) {
            rezultate = rezultate.stream()
                    .filter(a -> a.getStatus() == status)
                    .collect(Collectors.toList());
        }

        String pretMinStr = txtPretMin.getText().trim();
        if (!pretMinStr.isEmpty()) {
            try {
                double pretMin = Double.parseDouble(pretMinStr);
                rezultate = rezultate.stream()
                        .filter(a -> a.getPret() >= pretMin)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                lblRezultateAbonamente.setText(
                        "Prețul minim trebuie să fie un număr.");
                return;
            }
        }

        String pretMaxStr = txtPretMax.getText().trim();
        if (!pretMaxStr.isEmpty()) {
            try {
                double pretMax = Double.parseDouble(pretMaxStr);
                rezultate = rezultate.stream()
                        .filter(a -> a.getPret() <= pretMax)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                lblRezultateAbonamente.setText(
                        "Prețul maxim trebuie să fie un număr.");
                return;
            }
        }

        LocalDate expiraDupa = dpExpiraDupa.getValue();
        if (expiraDupa != null) {
            rezultate = rezultate.stream()
                    .filter(a -> a.getDataExpirare().isAfter(expiraDupa))
                    .collect(Collectors.toList());
        }

        // Filtru expira inainte de
        LocalDate expiraInainte = dpExpinaInainte.getValue();
        if (expiraInainte != null) {
            rezultate = rezultate.stream()
                    .filter(a -> a.getDataExpirare().isBefore(expiraInainte))
                    .collect(Collectors.toList());
        }

        tabelAbonamente.setItems(FXCollections.observableArrayList(rezultate));
        lblRezultateAbonamente.setText(
                "Găsite: " + rezultate.size() + " abonamente");
    }

    @FXML
    public void resetaAbonamente() {
        cmbTipAb.setValue(null);
        cmbStatusAb.setValue(null);
        txtPretMin.clear();
        txtPretMax.clear();
        dpExpiraDupa.setValue(null);
        dpExpinaInainte.setValue(null);
        List<Abonament> toate = abonamentService.toateAbonamentele();
        tabelAbonamente.setItems(FXCollections.observableArrayList(toate));
        lblRezultateAbonamente.setText(
                "Total: " + toate.size() + " abonamente");
    }
}