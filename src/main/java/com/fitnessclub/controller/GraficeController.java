package com.fitnessclub.controller;

import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.service.AbonamentService;
import com.fitnessclub.service.AntrenamentService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraficeController {

    @FXML private PieChart pieVenituri;
    @FXML private BarChart<String, Number> barAbonamente;
    @FXML private BarChart<String, Number> barAntrenamente;

    private final AbonamentService abonamentService =
            new AbonamentService();
    private final AntrenamentService antrenamentService =
            new AntrenamentService();

    @FXML
    public void initialize() {
        incarcaGrafice();
    }

    @FXML
    public void reimprospateaza() {
        pieVenituri.getData().clear();
        barAbonamente.getData().clear();
        barAntrenamente.getData().clear();
        incarcaGrafice();
    }

    private void incarcaGrafice() {
        incarcaPieVenituri();
        incarcaBarAbonamente();
        incarcaBarAntrenamente();
    }

    private void incarcaPieVenituri() {
        Map<TipAbonament, Double> venituri =
                abonamentService.venituriPeTip();
        double total = venituri.values().stream()
                .mapToDouble(Double::doubleValue).sum();

        for (TipAbonament tip : TipAbonament.values()) {
            double suma = venituri.getOrDefault(tip, 0.0);
            if (suma == 0) continue;
            double procent = total > 0 ? suma / total * 100 : 0;
            pieVenituri.getData().add(new PieChart.Data(
                    tip.name() + " (" + String.format("%.1f", procent) + "%)",
                    suma));
        }

        pieVenituri.setLegendVisible(true);
        pieVenituri.setLabelsVisible(true);
        pieVenituri.setClockwise(true);
        pieVenituri.setStartAngle(90);
    }

    private void incarcaBarAbonamente() {
        List<Abonament> toate = abonamentService.toateAbonamentele();
        Map<TipAbonament, Long> numarPeTip = toate.stream()
                .collect(Collectors.groupingBy(
                        Abonament::getTip, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nr. Abonamente");

        for (TipAbonament tip : TipAbonament.values()) {
            series.getData().add(new XYChart.Data<>(
                    tip.name(),
                    numarPeTip.getOrDefault(tip, 0L)));
        }

        barAbonamente.getData().add(series);
        barAbonamente.setLegendVisible(false);
    }

    private void incarcaBarAntrenamente() {
        List<Antrenament> primele10 = antrenamentService
                .toateAntrenamentele().stream()
                .limit(10)
                .collect(Collectors.toList());

        XYChart.Series<String, Number> seriesCapacitate =
                new XYChart.Series<>();
        seriesCapacitate.setName("Capacitate");

        XYChart.Series<String, Number> seriesParticipanti =
                new XYChart.Series<>();
        seriesParticipanti.setName("Participanți");

        for (Antrenament a : primele10) {
            String nume = a.getDenumire().length() > 12
                    ? a.getDenumire().substring(0, 12) + "..."
                    : a.getDenumire();
            seriesCapacitate.getData().add(
                    new XYChart.Data<>(nume, a.getCapacitate()));
            seriesParticipanti.getData().add(
                    new XYChart.Data<>(nume,
                            a.getParticipanti().size()));
        }

        barAntrenamente.getData().addAll(
                seriesCapacitate, seriesParticipanti);
        barAntrenamente.setLegendVisible(true);
    }
}