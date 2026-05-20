package com.fitnessclub.controller;

import com.fitnessclub.enums.Specialitate;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Antrenor;
import com.fitnessclub.service.AntrenorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class AntrenorController {

    @FXML private TextField txtNume, txtPrenume, txtEmail, txtTelefon, txtSalariu;
    @FXML private ComboBox<Specialitate> cmbSpecialitate;
    @FXML private TableView<Antrenor> tabelAntrenori;
    @FXML private TableColumn<Antrenor, String> colId, colNume, colPrenume,
            colEmail, colTelefon, colSpecialitate, colSalariu;
    @FXML private Label lblMesaj;

    private final AntrenorService antrenorService = new AntrenorService();
    private Antrenor antrenorSelectat;

    @FXML
    public void initialize() {
        cmbSpecialitate.setItems(FXCollections.observableArrayList(Specialitate.values()));
        cmbFiltru.setItems(FXCollections.observableArrayList(Specialitate.values()));
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colNume.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNume()));
        colPrenume.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrenume()));
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEmail()));
        colTelefon.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTelefon()));
        colSpecialitate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getSpecialitate().name()));
        colSalariu.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f lei", d.getValue().getSalariu())));

        tabelAntrenori.getSelectionModel().selectedItemProperty().addListener(
                (obs, vechi, nou) -> {
                    if (nou != null) {
                        antrenorSelectat = nou;
                        txtNume.setText(nou.getNume());
                        txtPrenume.setText(nou.getPrenume());
                        txtEmail.setText(nou.getEmail());
                        txtTelefon.setText(nou.getTelefon());
                        txtSalariu.setText(String.valueOf(nou.getSalariu()));
                        cmbSpecialitate.setValue(nou.getSpecialitate());
                    }
                });

        incarcaAntrenori();
    }

    @FXML
    public void incarcaAntrenori() {
        tabelAntrenori.setItems(FXCollections.observableArrayList(
                antrenorService.totiAntrenorii()));
        setMesaj("", false);
    }

    @FXML
    public void adaugaAntrenor() {
        try {
            double salariu = Double.parseDouble(txtSalariu.getText());
            antrenorService.adaugaAntrenor(
                    txtNume.getText(), txtPrenume.getText(),
                    txtEmail.getText(), txtTelefon.getText(),
                    cmbSpecialitate.getValue(), salariu);
            incarcaAntrenori();
            golesteCampuri();
            setMesaj("Antrenor adăugat.", false);
        } catch (NumberFormatException e) {
            setMesaj("Salariul trebuie să fie un număr valid.", true);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void actualizeazaAntrenor() {
        if (antrenorSelectat == null) {
            setMesaj("Selectează un antrenor.", true);
            return;
        }
        try {
            double salariu = Double.parseDouble(txtSalariu.getText());
            antrenorService.actualizeazaAntrenor(
                    antrenorSelectat.getId(),
                    txtNume.getText(), txtPrenume.getText(),
                    txtEmail.getText(), txtTelefon.getText(),
                    cmbSpecialitate.getValue(), salariu);
            incarcaAntrenori();
            golesteCampuri();
            setMesaj("Antrenor actualizat.", false);
        } catch (NumberFormatException e) {
            setMesaj("Salariul trebuie să fie un număr valid.", true);
        } catch (ValidationException e) {
            setMesaj(e.getMessage(), true);
        }
    }

    @FXML
    public void stergeAntrenor() {
        if (antrenorSelectat == null) {
            setMesaj("Selectează un antrenor.", true);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare ștergere");
        confirm.setHeaderText("Ștergi antrenorul: " + antrenorSelectat.getPrenume()
                + " " + antrenorSelectat.getNume() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                antrenorService.stergeAntrenor(antrenorSelectat.getId());
                incarcaAntrenori();
                golesteCampuri();
                setMesaj("Antrenor șters.", false);
            }
        });
    }

    @FXML
    public void golesteCampuri() {
        txtNume.clear(); txtPrenume.clear(); txtEmail.clear();
        txtTelefon.clear(); txtSalariu.clear();
        cmbSpecialitate.setValue(null);
        antrenorSelectat = null;
        tabelAntrenori.getSelectionModel().clearSelection();
    }

    private void setMesaj(String mesaj, boolean eroare) {
        lblMesaj.setText(mesaj);
        lblMesaj.getStyleClass().removeAll("label-error", "label-success");
        lblMesaj.getStyleClass().add(eroare ? "label-error" : "label-success");
    }

    @FXML private ComboBox<Specialitate> cmbFiltru;

    @FXML
    public void filtreazaDupaSpecialitate() {
        Specialitate spec = cmbFiltru.getValue();
        if (spec == null) {
            incarcaAntrenori();
            return;
        }
        List<Antrenor> filtrati = antrenorService.filtreazaDupaSpecialitate(spec);
        tabelAntrenori.setItems(FXCollections.observableArrayList(filtrati));
        setMesaj("Afișați: " + filtrati.size() + " antrenori cu specialitatea " + spec, false);
    }
}