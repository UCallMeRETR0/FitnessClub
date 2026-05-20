package com.fitnessclub.controller;

import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.service.AbonamentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AbonamentController {

    @FXML private TextField txtClientId, txtPret;
    @FXML private ComboBox<TipAbonament> cmbTip;
    @FXML private ComboBox<StatusAbonament> cmbFiltruStatus;
    @FXML private DatePicker dpDataStart;
    @FXML private TableView<Abonament> tabelAbonamente;
    @FXML private TableColumn<Abonament, String> colId, colClient, colTip,
            colStatus, colStart, colExpirare, colPret, colZile;
    @FXML private Label lblMesaj;

    private final AbonamentService abonamentService = new AbonamentService();
    private Abonament abonamentSelectat;

    @FXML
    public void initialize() {
        cmbTip.setItems(FXCollections.observableArrayList(TipAbonament.values()));
        cmbFiltruStatus.setItems(FXCollections.observableArrayList(StatusAbonament.values()));
        dpDataStart.setValue(LocalDate.now());

        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colClient.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getClient().getPrenume() + " " + d.getValue().getClient().getNume()));
        colTip.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTip().name()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().name()));
        colStart.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataStart().toString()));
        colExpirare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataExpirare().toString()));
        colPret.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f lei", d.getValue().getPret())));
        colZile.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().esteActiv() ? d.getValue().zileRamase() + " zile" : "Expirat"));

        tabelAbonamente.getSelectionModel().selectedItemProperty().addListener(
                (obs, vechi, nou) -> {
                    if (nou != null) {
                        abonamentSelectat = nou;
                        txtClientId.setText(String.valueOf(nou.getClient().getId()));
                        cmbTip.setValue(nou.getTip());
                        dpDataStart.setValue(nou.getDataStart());
                        txtPret.setText(String.valueOf(nou.getPret()));
                    }
                });

        incarcaAbonamente();
    }

    @FXML
    public void incarcaAbonamente() {
        tabelAbonamente.setItems(FXCollections.observableArrayList(
                abonamentService.toateAbonamentele()));
        setMesaj("", false);
    }

    @FXML
    public void adaugaAbonament() {
        try {
            int clientId = Integer.parseInt(txtClientId.getText().trim());
            double pret = Double.parseDouble(txtPret.getText().trim());
            abonamentService.adaugaAbonament(clientId, cmbTip.getValue(),
                    dpDataStart.getValue(), pret);
            incarcaAbonamente();
            golesteCampuri();
            setMesaj("Abonament adăugat.", false);
        } catch (NumberFormatException e) {
            setMesaj("ID client și prețul trebuie să fie numere valide.", true);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void stergeAbonament() {
        if (abonamentSelectat == null) {
            setMesaj("Selectează un abonament.", true);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare ștergere");
        confirm.setHeaderText("Ștergi abonamentul #" + abonamentSelectat.getId() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                abonamentService.stergeAbonament(abonamentSelectat.getId());
                incarcaAbonamente();
                golesteCampuri();
                setMesaj("Abonament șters.", false);
            }
        });
    }

    @FXML
    public void filtreazaDupaStatus() {
        StatusAbonament status = cmbFiltruStatus.getValue();
        if (status == null) {
            incarcaAbonamente();
            return;
        }
        List<Abonament> filtrate = abonamentService.abonamenteDupaStatus(status);
        tabelAbonamente.setItems(FXCollections.observableArrayList(filtrate));
        setMesaj("Afișate: " + filtrate.size() + " abonamente cu status " + status, false);
    }

    @FXML
    public void golesteCampuri() {
        txtClientId.clear();
        txtPret.clear();
        cmbTip.setValue(null);
        dpDataStart.setValue(LocalDate.now());
        abonamentSelectat = null;
        tabelAbonamente.getSelectionModel().clearSelection();
    }

    private void setMesaj(String mesaj, boolean eroare) {
        lblMesaj.setText(mesaj);
        lblMesaj.getStyleClass().removeAll("label-error", "label-success");
        lblMesaj.getStyleClass().add(eroare ? "label-error" : "label-success");
    }
}