package com.fitnessclub.controller;

import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Client;
import com.fitnessclub.service.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ClientController {

    @FXML private TextField txtNume, txtPrenume, txtEmail, txtTelefon, txtCautare;
    @FXML private DatePicker dpDataNasterii;
    @FXML private TableView<Client> tabelClienti;
    @FXML private TableColumn<Client, String> colId, colNume, colPrenume,
            colEmail, colTelefon, colDataNasterii;
    @FXML private Label lblMesaj;

    private final ClientService clientService = new ClientService();
    private Client clientSelectat;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getId())));
        colNume.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNume()));
        colPrenume.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPrenume()));
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEmail()));
        colTelefon.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTelefon()));
        colDataNasterii.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataNasterii().toString()));

        tabelClienti.getSelectionModel().selectedItemProperty().addListener(
                (obs, vechi, nou) -> {
                    if (nou != null) {
                        clientSelectat = nou;
                        txtNume.setText(nou.getNume());
                        txtPrenume.setText(nou.getPrenume());
                        txtEmail.setText(nou.getEmail());
                        txtTelefon.setText(nou.getTelefon());
                        dpDataNasterii.setValue(nou.getDataNasterii());
                    }
                });

        incarcaClienti();
    }

    @FXML
    public void incarcaClienti() {
        List<Client> clienti = clientService.totiClientii();
        tabelClienti.setItems(FXCollections.observableArrayList(clienti));
        setMesaj("", false);
    }

    @FXML
    public void adaugaClient() {
        try {
            clientService.adaugaClient(
                    txtNume.getText(), txtPrenume.getText(),
                    txtEmail.getText(), txtTelefon.getText(),
                    dpDataNasterii.getValue());
            incarcaClienti();
            golesteCampuri();
            setMesaj("Client adăugat cu succes.", false);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void actualizeazaClient() {
        if (clientSelectat == null) {
            setMesaj("Selectează un client din tabel.", true);
            return;
        }
        try {
            clientService.actualizeazaClient(
                    clientSelectat.getId(),
                    txtNume.getText(), txtPrenume.getText(),
                    txtEmail.getText(), txtTelefon.getText(),
                    dpDataNasterii.getValue());
            incarcaClienti();
            golesteCampuri();
            setMesaj("Client actualizat.", false);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void stergeClient() {
        if (clientSelectat == null) {
            setMesaj("Selectează un client din tabel.", true);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare ștergere");
        confirm.setHeaderText("Ștergi clientul: " + clientSelectat.getPrenume()
                + " " + clientSelectat.getNume() + "?");
        confirm.setContentText("Această acțiune este ireversibilă.");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                clientService.stergeClient(clientSelectat.getId());
                incarcaClienti();
                golesteCampuri();
                setMesaj("Client șters.", false);
            }
        });
    }

    @FXML
    public void cautaClient() {
        String text = txtCautare.getText().trim();
        if (text.isEmpty()) {
            incarcaClienti();
            return;
        }
        List<Client> rezultate = clientService.cautaDupaNume(text);
        tabelClienti.setItems(FXCollections.observableArrayList(rezultate));
    }

    @FXML
    public void golesteCampuri() {
        txtNume.clear();
        txtPrenume.clear();
        txtEmail.clear();
        txtTelefon.clear();
        dpDataNasterii.setValue(null);
        clientSelectat = null;
        tabelClienti.getSelectionModel().clearSelection();
    }



    private void setMesaj(String mesaj, boolean eroare) {
        lblMesaj.setText(mesaj);
        lblMesaj.getStyleClass().removeAll("label-error", "label-success");
        lblMesaj.getStyleClass().add(eroare ? "label-error" : "label-success");
    }
}