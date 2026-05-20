package com.fitnessclub.controller;

import com.fitnessclub.util.NotificareUtil;

import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.service.AbonamentService;
import com.fitnessclub.service.AntrenamentService;
import com.fitnessclub.service.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label lblClientiActivi, lblVenituri, lblAbonamenteActive,
            lblExpiraCurand, lblAntrenamente;
    @FXML private Label lblTopAntrenament, lblTopParticipanti;

    @FXML private TableView<Abonament> tabelExpirare;
    @FXML private TableColumn<Abonament, String> colExpClient, colExpTip,
            colExpData, colExpZile;

    @FXML private TableView<String[]> tabelVenituri;
    @FXML private TableColumn<String[], String> colTip, colNr, colSuma, colProcent;

    private final ClientService clientService       = new ClientService();
    private final AbonamentService abonamentService = new AbonamentService();
    private final AntrenamentService antrenamentService = new AntrenamentService();

    @FXML
    public void initialize() {
        initColoane();
        incarcaDate();
    }

    @FXML
    public void verificaNotificari() {
        NotificareUtil.verificaExpirari();
    }

    @FXML
    public void reimprospateaza() {
        incarcaDate();
        setMesaj("Date actualizate.", false);
    }

    @FXML private Label lblMesaj; // adauga si in fxml: <Label fx:id="lblMesaj" styleClass="label-success"/>

    private void setMesaj(String mesaj, boolean eroare) {
        lblMesaj.setText(mesaj);
        lblMesaj.getStyleClass().removeAll("label-error", "label-success");
        lblMesaj.getStyleClass().add(eroare ? "label-error" : "label-success");
    }

    private void initColoane() {
        colExpClient.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getClient().getPrenume() + " "
                        + d.getValue().getClient().getNume()));
        colExpTip.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTip().name()));
        colExpData.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataExpirare().toString()));
        colExpZile.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().zileRamase() + " zile"));

        colTip.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        colNr.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        colSuma.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        colProcent.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
    }

    private void incarcaDate() {
        // Carduri
        int clientiActivi = clientService.clientiCuAbonamentActiv().size();
        lblClientiActivi.setText(String.valueOf(clientiActivi));

        double venituri = abonamentService.venitriTotale();
        lblVenituri.setText(String.format("%.0f lei", venituri));

        int abonamenteActive = abonamentService
                .abonamenteDupaStatus(StatusAbonament.ACTIV).size();
        lblAbonamenteActive.setText(String.valueOf(abonamenteActive));

        int expiraCurand = abonamentService.abonamenteCareExpiraCurand(7).size();
        lblExpiraCurand.setText(String.valueOf(expiraCurand));

        int totalAntrenamente = antrenamentService.toateAntrenamentele().size();
        lblAntrenamente.setText(String.valueOf(totalAntrenamente));

        // Top antrenament cu cei mai multi participanti
        antrenamentService.toateAntrenamentele().stream()
                .max(Comparator.comparingInt(a ->
                        antrenamentService.toateAntrenamentele()
                                .stream().mapToInt(Antrenament::getCapacitate)
                                .max().orElse(0)))
                .ifPresent(a -> {
                    lblTopAntrenament.setText(a.getDenumire()
                            + " — " + a.getAntrenor().getPrenume()
                            + " " + a.getAntrenor().getNume());
                    lblTopParticipanti.setText("Capacitate: "
                            + a.getCapacitate() + " locuri");
                });

        // Tabel expirare curand
        List<Abonament> expira = abonamentService.abonamenteCareExpiraCurand(7);
        tabelExpirare.setItems(FXCollections.observableArrayList(expira));

        // Tabel venituri pe tip
        Map<TipAbonament, Long> numarPeTip = abonamentService.toateAbonamentele()
                .stream()
                .collect(Collectors.groupingBy(Abonament::getTip,
                        Collectors.counting()));

        Map<TipAbonament, Double> venituriPeTip =
                abonamentService.venituriPeTip();

        ObservableList<String[]> rows = FXCollections.observableArrayList();
        for (TipAbonament tip : TipAbonament.values()) {
            double suma = venituriPeTip.getOrDefault(tip, 0.0);
            long nr = numarPeTip.getOrDefault(tip, 0L);
            double procent = venituri > 0 ? (suma / venituri * 100) : 0;
            rows.add(new String[]{
                    tip.name(),
                    String.valueOf(nr),
                    String.format("%.2f lei", suma),
                    String.format("%.1f%%", procent)
            });
        }




        tabelVenituri.setItems(rows);
    }
}