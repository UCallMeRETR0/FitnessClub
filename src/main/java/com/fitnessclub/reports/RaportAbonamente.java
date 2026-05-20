package com.fitnessclub.reports;

import com.fitnessclub.dao.AbonamentDAO;
import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.interfaces.Exportabil;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.util.ExportUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RaportAbonamente implements Exportabil {

    private final AbonamentDAO abonamentDAO;
    private List<Abonament> date;

    public RaportAbonamente() {
        this.abonamentDAO = new AbonamentDAO();
    }

    public List<Abonament> genereaza() {
        date = abonamentDAO.findByStatus(StatusAbonament.ACTIV);
        System.out.println("\n=== RAPORT: Abonamente Active ===");
        System.out.printf("%-5s %-15s %-20s %-12s %-12s %-10s%n",
                "ID", "Tip", "Client", "Start", "Expirare", "Pret");
        System.out.println("-".repeat(80));
        for (Abonament a : date) {
            System.out.printf("%-5d %-15s %-20s %-12s %-12s %.2f lei%n",
                    a.getId(),
                    a.getTip(),
                    a.getClient().getPrenume() + " " + a.getClient().getNume(),
                    a.getDataStart(),
                    a.getDataExpirare(),
                    a.getPret());
        }
        System.out.println("-".repeat(80));
        System.out.printf("Total abonamente active: %d%n", date.size());
        return date;
    }

    @Override
    public String exportTxt() {
        if (date == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_abonamente", "txt");
        List<String> linii = date.stream()
                .map(a -> String.format("%-5d | %-15s | %-20s | Expira: %s | %.2f lei",
                        a.getId(), a.getTip(),
                        a.getClient().getPrenume() + " " + a.getClient().getNume(),
                        a.getDataExpirare(), a.getPret()))
                .collect(Collectors.toList());
        ExportUtil.exportTxt(cale, "RAPORT ABONAMENTE ACTIVE", linii);
        return cale;
    }

    @Override
    public String exportCsv() {
        if (date == null) genereaza();
        String cale = ExportUtil.numeeFisier("raport_abonamente", "csv");
        String header = "ID,Tip,Client,DataStart,DataExpirare,Pret,Status";
        List<String> linii = date.stream()
                .map(Abonament::exportCsv)
                .collect(Collectors.toList());
        ExportUtil.exportCsv(cale, header, linii);
        return cale;
    }
}