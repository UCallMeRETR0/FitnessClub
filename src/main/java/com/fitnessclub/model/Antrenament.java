package com.fitnessclub.model;

import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.interfaces.Exportabil;
import com.fitnessclub.util.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Antrenament implements Exportabil {
    private int id;
    private String denumire;
    private LocalDateTime data;
    private int durata;
    private int capacitate;
    private Antrenor antrenor;
    private List<Client> participanti;

    public Antrenament(int id, String denumire, LocalDateTime data, int durata, int capacitate, Antrenor antrenor) {
        Validator.validareTextObligatoriu(denumire, "denumire");
        Validator.validareCapacitate(capacitate);
        Validator.validareDurata(durata);
        if (data == null)
            throw new ValidationException("data", "data antrenamentului nu poate fi nulă.");
        if (antrenor == null)
            throw new ValidationException("antrenor", "antrenorul nu poate fi nul.");

        this.id = id;
        this.denumire = denumire;
        this.data = data;
        this.durata = durata;
        this.capacitate = capacitate;
        this.antrenor = antrenor;
        this.participanti = new ArrayList<>();
    }

    public boolean adaugaParticipant(Client c) {
        if (participanti.size() >= capacitate) return false;
        participanti.add(c);
        return true;
    }

    public int locuriDisponibile() { return capacitate - participanti.size(); }

    public int getId() { return id; }
    public String getDenumire() { return denumire; }
    public LocalDateTime getData() { return data; }
    public int getDurata() { return durata; }
    public int getCapacitate() { return capacitate; }
    public Antrenor getAntrenor() { return antrenor; }
    public List<Client> getParticipanti() { return participanti; }

    @Override
    public String exportTxt() {
        return String.format("Antrenament: %s | Data: %s | Antrenor: %s %s | Participanti: %d/%d",
                denumire, data, antrenor.getPrenume(), antrenor.getNume(),
                participanti.size(), capacitate);
    }

    @Override
    public String exportCsv() {
        return id + "," + denumire + "," + data + "," + antrenor.getNume() + ","
                + participanti.size() + "," + capacitate;
    }
}