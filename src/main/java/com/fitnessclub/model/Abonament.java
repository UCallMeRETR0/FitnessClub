package com.fitnessclub.model;

import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.interfaces.Exportabil;
import com.fitnessclub.util.Validator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Abonament implements Exportabil {
    private int id;
    private TipAbonament tip;
    private StatusAbonament status;
    private LocalDate dataStart;
    private LocalDate dataExpirare;
    private double pret;
    private Client client;

    public Abonament(int id, TipAbonament tip, LocalDate dataStart, double pret, Client client) {
        Validator.validarePret(pret, "pret");
        Validator.validareDataStart(dataStart);
        if (tip == null)
            throw new ValidationException("tip", "tipul abonamentului nu poate fi nul.");
        if (client == null)
            throw new ValidationException("client", "clientul nu poate fi nul.");

        this.id = id;
        this.tip = tip;
        this.dataStart = dataStart;
        this.pret = pret;
        this.client = client;
        this.status = StatusAbonament.ACTIV;

        switch (tip) {
            case LUNAR:       this.dataExpirare = dataStart.plusMonths(1); break;
            case TRIMESTRIAL: this.dataExpirare = dataStart.plusMonths(3); break;
            case ANUAL:       this.dataExpirare = dataStart.plusYears(1);  break;
            case ZI_UNICA:    this.dataExpirare = dataStart.plusDays(1);   break;
        }
    }

    public boolean esteActiv() {
        return status == StatusAbonament.ACTIV && LocalDate.now().isBefore(dataExpirare);
    }

    public long zileRamase() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataExpirare);
    }

    public int getId() { return id; }
    public TipAbonament getTip() { return tip; }
    public StatusAbonament getStatus() { return status; }
    public LocalDate getDataStart() { return dataStart; }
    public LocalDate getDataExpirare() { return dataExpirare; }
    public double getPret() { return pret; }
    public Client getClient() { return client; }
    public void setStatus(StatusAbonament status) { this.status = status; }

    @Override
    public String exportTxt() {
        return String.format("Abonament #%d | Tip: %s | Pret: %.2f lei | Expira: %s | Status: %s",
                id, tip, pret, dataExpirare, status);
    }

    @Override
    public String exportCsv() {
        return id + "," + tip + "," + pret + "," + dataExpirare + "," + status;
    }
}