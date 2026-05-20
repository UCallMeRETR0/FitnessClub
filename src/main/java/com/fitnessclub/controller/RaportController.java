package com.fitnessclub.controller;

import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.reports.RaportAbonamente;
import com.fitnessclub.reports.RaportAntrenamente;
import com.fitnessclub.reports.RaportVenituri;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaportController {

    @FXML private TableView<String[]> tabelRaport;
    @FXML private TableColumn<String[], String> colR1, colR2, colR3, colR4;
    @FXML private Label lblMesajExport, lblTotalAbonamente,
            lblTotalVenituri, lblTotalAntrenamente, lblRaportActiv;

    private RaportAbonamente raportAbonamente;
    private RaportVenituri raportVenituri;
    private RaportAntrenamente raportAntrenamente;

    @FXML
    public void initialize() {
        raportAbonamente  = new RaportAbonamente();
        raportVenituri    = new RaportVenituri();
        raportAntrenamente = new RaportAntrenamente();

        colR1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().length > 0 ? d.getValue()[0] : ""));
        colR2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().length > 1 ? d.getValue()[1] : ""));
        colR3.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().length > 2 ? d.getValue()[2] : ""));
        colR4.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().length > 3 ? d.getValue()[3] : ""));
    }

    @FXML
    public void genereazaAbonamente() {
        try {
            List<Abonament> date = raportAbonamente.genereaza();
            lblTotalAbonamente.setText("Total: " + date.size());

            colR1.setText("ID");
            colR2.setText("Client");
            colR3.setText("Tip");
            colR4.setText("Expiră");

            ObservableList<String[]> rows = FXCollections.observableArrayList();
            for (Abonament a : date) {
                rows.add(new String[]{
                        String.valueOf(a.getId()),
                        a.getClient().getPrenume() + " " + a.getClient().getNume(),
                        a.getTip().name(),
                        a.getDataExpirare().toString()
                });
            }
            tabelRaport.setItems(rows);
            lblRaportActiv.setText("Raport: Abonamente Active — " + date.size() + " înregistrări");
            setMesaj("", false);
        } catch (Exception e) {
            setMesaj("Eroare: " + e.getMessage(), true);
        }
    }

    @FXML
    public void genereazaVenituri() {
        try {
            Map<?, Double> date = raportVenituri.genereaza();
            lblTotalVenituri.setText(String.format("Total: %.2f lei", raportVenituri.getTotalVenituri()));

            colR1.setText("Tip Abonament");
            colR2.setText("Venituri (lei)");
            colR3.setText("");
            colR4.setText("");

            ObservableList<String[]> rows = FXCollections.observableArrayList();
            for (Map.Entry<?, Double> e : date.entrySet()) {
                rows.add(new String[]{
                        e.getKey().toString(),
                        String.format("%.2f lei", e.getValue()),
                        "", ""
                });
            }
            tabelRaport.setItems(rows);
            lblRaportActiv.setText("Raport: Venituri pe Tip Abonament");
            setMesaj("", false);
        } catch (Exception e) {
            setMesaj("Eroare: " + e.getMessage(), true);
        }
    }

    @FXML
    public void genereazaAntrenamente() {
        try {
            List<Antrenament> date = raportAntrenamente.genereaza();
            lblTotalAntrenamente.setText("Total: " + date.size());

            colR1.setText("ID");
            colR2.setText("Denumire");
            colR3.setText("Antrenor");
            colR4.setText("Capacitate");

            ObservableList<String[]> rows = FXCollections.observableArrayList();
            for (Antrenament a : date) {
                rows.add(new String[]{
                        String.valueOf(a.getId()),
                        a.getDenumire(),
                        a.getAntrenor().getPrenume() + " " + a.getAntrenor().getNume(),
                        a.getCapacitate() + " locuri"
                });
            }
            tabelRaport.setItems(rows);
            lblRaportActiv.setText("Raport: Antrenamente — " + date.size() + " înregistrări");
            setMesaj("", false);
        } catch (Exception e) {
            setMesaj("Eroare: " + e.getMessage(), true);
        }
    }

    @FXML public void exportAbonamenteTxt() {
        try {
            String cale = raportAbonamente.exportTxt();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    @FXML public void exportAbonamenteCsv() {
        try {
            String cale = raportAbonamente.exportCsv();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    @FXML public void exportVenituriTxt() {
        try {
            String cale = raportVenituri.exportTxt();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    @FXML public void exportVenituriCsv() {
        try {
            String cale = raportVenituri.exportCsv();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    @FXML public void exportAntrenamenteTxt() {
        try {
            String cale = raportAntrenamente.exportTxt();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    @FXML public void exportAntrenamenteCsv() {
        try {
            String cale = raportAntrenamente.exportCsv();
            setMesaj("Exportat: " + cale, false);
        } catch (Exception e) {
            setMesaj("Eroare export: " + e.getMessage(), true);
        }
    }

    private void setMesaj(String mesaj, boolean eroare) {
        lblMesajExport.setText(mesaj);
        lblMesajExport.getStyleClass().removeAll("label-error", "label-success");
        lblMesajExport.getStyleClass().add(eroare ? "label-error" : "label-success");
    }
}