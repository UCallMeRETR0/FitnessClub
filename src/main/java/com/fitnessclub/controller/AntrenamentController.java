package com.fitnessclub.controller;

import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.service.AntrenamentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AntrenamentController {

    @FXML private TextField txtDenumire, txtAntrenorId, txtDurata, txtCapacitate, txtCautare;
    @FXML private DatePicker dpData;
    @FXML private TableView<Antrenament> tabelAntrenamente;
    @FXML private TableColumn<Antrenament, String> colId, colDenumire, colAntrenor,
            colData, colDurata, colCapacitate, colLocuri;
    @FXML private Label lblMesaj;

    private final AntrenamentService antrenamentService = new AntrenamentService();
    private Antrenament antrenamentSelectat;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colDenumire.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDenumire()));
        colAntrenor.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getAntrenor().getPrenume() + " " + d.getValue().getAntrenor().getNume()));
        colData.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getData().toString()));
        colDurata.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDurata() + " min"));
        colCapacitate.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCapacitate())));
        colLocuri.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().locuriDisponibile())));

        tabelAntrenamente.getSelectionModel().selectedItemProperty().addListener(
                (obs, vechi, nou) -> {
                    if (nou != null) {
                        antrenamentSelectat = nou;
                        txtDenumire.setText(nou.getDenumire());
                        txtAntrenorId.setText(String.valueOf(nou.getAntrenor().getId()));
                        dpData.setValue(nou.getData().toLocalDate());
                        txtDurata.setText(String.valueOf(nou.getDurata()));
                        txtCapacitate.setText(String.valueOf(nou.getCapacitate()));
                    }
                });

        incarcaAntrenamente();
    }

    @FXML
    public void incarcaAntrenamente() {
        tabelAntrenamente.setItems(FXCollections.observableArrayList(
                antrenamentService.toateAntrenamentele()));
        setMesaj("", false);
    }

    @FXML
    public void adaugaAntrenament() {
        try {
            int antrenorId = Integer.parseInt(txtAntrenorId.getText().trim());
            int durata = Integer.parseInt(txtDurata.getText().trim());
            int capacitate = Integer.parseInt(txtCapacitate.getText().trim());
            LocalDate data = dpData.getValue();
            if (data == null) throw new ValidationException("data", "Data este obligatorie.");
            LocalDateTime dataTime = data.atTime(9, 0);

            antrenamentService.adaugaAntrenament(txtDenumire.getText(),
                    dataTime, durata, capacitate, antrenorId);
            incarcaAntrenamente();
            golesteCampuri();
            setMesaj("Antrenament adăugat.", false);
        } catch (NumberFormatException e) {
            setMesaj("ID antrenor, durata și capacitatea trebuie să fie numere.", true);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void actualizeazaAntrenament() {
        if (antrenamentSelectat == null) {
            setMesaj("Selectează un antrenament.", true);
            return;
        }
        try {
            int antrenorId = Integer.parseInt(txtAntrenorId.getText().trim());
            int durata = Integer.parseInt(txtDurata.getText().trim());
            int capacitate = Integer.parseInt(txtCapacitate.getText().trim());
            LocalDateTime dataTime = dpData.getValue().atTime(9, 0);

            antrenamentService.actualizeazaAntrenament(antrenamentSelectat.getId(),
                    txtDenumire.getText(), dataTime, durata, capacitate, antrenorId);
            incarcaAntrenamente();
            golesteCampuri();
            setMesaj("Antrenament actualizat.", false);
        } catch (NumberFormatException e) {
            setMesaj("Valori numerice invalide.", true);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void stergeAntrenament() {
        if (antrenamentSelectat == null) {
            setMesaj("Selectează un antrenament.", true);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare ștergere");
        confirm.setHeaderText("Ștergi antrenamentul: " + antrenamentSelectat.getDenumire() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                antrenamentService.stergeAntrenament(antrenamentSelectat.getId());
                incarcaAntrenamente();
                golesteCampuri();
                setMesaj("Antrenament șters.", false);
            }
        });
    }

    @FXML
    public void cautaAntrenament() {
        String text = txtCautare.getText().trim();
        if (text.isEmpty()) {
            incarcaAntrenamente();
            return;
        }
        List<Antrenament> rezultate = antrenamentService.cautaDupaDenumire(text);
        tabelAntrenamente.setItems(FXCollections.observableArrayList(rezultate));
    }

    @FXML
    public void golesteCampuri() {
        txtDenumire.clear(); txtAntrenorId.clear();
        txtDurata.clear(); txtCapacitate.clear();
        dpData.setValue(null);
        antrenamentSelectat = null;
        tabelAntrenamente.getSelectionModel().clearSelection();
    }

    private void setMesaj(String mesaj, boolean eroare) {
        lblMesaj.setText(mesaj);
        lblMesaj.getStyleClass().removeAll("label-error", "label-success");
        lblMesaj.getStyleClass().add(eroare ? "label-error" : "label-success");
    }
}