package com.fitnessclub.reports;

import com.fitnessclub.dao.AbonamentDAO;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.interfaces.Exportabil;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.util.ExportUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RaportVenituri implements Exportabil {

    private final AbonamentDAO abonamentDAO;
    private Map<TipAbonament, Double> venituriPeTip;
    private double totalVenituri;

    public RaportVenituri() {
        this.abonamentDAO = new AbonamentDAO();
    }

    public Map<TipAbonament, Double> genereaza() {
        List<Abonament> toate = abonamentDAO.findAll();
        venituriPeTip = toate.stream()
                .collect(Collectors.groupingBy(
                        Abonament::getTip,
                        Collectors.summingDouble(Abonament::getPret)
                ));
        totalVenituri = toate.stream().mapToDouble(Abonament::getPret).sum();

        System.out.println("\n=== RAPORT: Venituri pe Tip Abonament ===");
        System.out.printf("%-20s %-15s%n", "Tip Abonament", "Venituri (lei)");
        System.out.println("-".repeat(40));
        venituriPeTip.forEach((tip, suma) ->
                System.out.printf("%-20s %.2f lei%n", tip, suma));
        System.out.println("-".repeat(40));
        System.out.printf("TOTAL: %.2f lei%n", totalVenituri);
        return venituriPeTip;
    }

    public double getTotalVenituri() { return totalVenituri; }

    @Override
    public String exportTxt() {
        if (venituriPeTip == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_venituri", "txt");
        List<String> linii = new ArrayList<>();
        venituriPeTip.forEach((tip, suma) ->
                linii.add(String.format("%-20s : %.2f lei", tip, suma)));
        linii.add("-".repeat(40));
        linii.add(String.format("%-20s : %.2f lei", "TOTAL", totalVenituri));
        ExportUtil.exportTxt(cale, "RAPORT VENITURI PE TIP ABONAMENT", linii);
        return cale;
    }

    @Override
    public String exportCsv() {
        if (venituriPeTip == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_venituri", "csv");
        String header = "TipAbonament,VenituriLei";
        List<String> linii = new ArrayList<>();
        venituriPeTip.forEach((tip, suma) ->
                linii.add(tip + "," + String.format("%.2f", suma)));
        ExportUtil.exportCsv(cale, header, linii);
        return cale;
    }
}