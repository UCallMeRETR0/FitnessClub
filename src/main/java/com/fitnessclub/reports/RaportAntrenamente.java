package com.fitnessclub.reports;

import com.fitnessclub.dao.AntrenamentDAO;
import com.fitnessclub.interfaces.Exportabil;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.util.ExportUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RaportAntrenamente implements Exportabil {

    private final AntrenamentDAO antrenamentDAO;
    private List<Antrenament> date;

    public RaportAntrenamente() {
        this.antrenamentDAO = new AntrenamentDAO();
    }

    public List<Antrenament> genereaza() {
        date = antrenamentDAO.findAll();
        System.out.println("\n=== RAPORT: Antrenamente ===");
        System.out.printf("%-5s %-20s %-20s %-10s %-15s%n",
                "ID", "Denumire", "Antrenor", "Durata", "Capacitate");
        System.out.println("-".repeat(75));
        for (Antrenament a : date) {
            System.out.printf("%-5d %-20s %-20s %-10d %d locuri%n",
                    a.getId(),
                    a.getDenumire(),
                    a.getAntrenor().getPrenume() + " " + a.getAntrenor().getNume(),
                    a.getDurata(),
                    a.getCapacitate());
        }
        System.out.println("-".repeat(75));
        System.out.printf("Total antrenamente: %d%n", date.size());
        return date;
    }

    @Override
    public String exportTxt() {
        if (date == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_antrenamente", "txt");
        List<String> linii = date.stream()
                .map(a -> String.format("%-5d | %-20s | Antrenor: %-20s | %d min | %d locuri",
                        a.getId(), a.getDenumire(),
                        a.getAntrenor().getPrenume() + " " + a.getAntrenor().getNume(),
                        a.getDurata(), a.getCapacitate()))
                .collect(Collectors.toList());
        ExportUtil.exportTxt(cale, "RAPORT ANTRENAMENTE", linii);
        return cale;
    }

    @Override
    public String exportCsv() {
        if (date == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_antrenamente", "csv");
        String header = "ID,Denumire,Data,Antrenor,Participanti,Capacitate";
        List<String> linii = date.stream()
                .map(Antrenament::exportCsv)
                .collect(Collectors.toList());
        ExportUtil.exportCsv(cale, header, linii);
        return cale;
    }
}