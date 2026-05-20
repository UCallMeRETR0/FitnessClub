package com.fitnessclub.service;

import com.fitnessclub.dao.AntrenamentDAO;
import com.fitnessclub.dao.AntrenorDAO;
import com.fitnessclub.dao.ClientDAO;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.model.Antrenor;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AntrenamentService {

    private final AntrenamentDAO antrenamentDAO;
    private final AntrenorDAO antrenorDAO;
    private final ClientDAO clientDAO;

    public AntrenamentService() {
        this.antrenamentDAO = new AntrenamentDAO();
        this.antrenorDAO = new AntrenorDAO();
        this.clientDAO = new ClientDAO();
    }

    public void adaugaAntrenament(String denumire, LocalDateTime data,
                                  int durata, int capacitate, int antrenorId) {
        Validator.validareTextObligatoriu(denumire, "denumire");
        Validator.validareCapacitate(capacitate);
        Validator.validareDurata(durata);

        Antrenor antrenor = antrenorDAO.findById(antrenorId);
        if (antrenor == null)
            throw new ValidationException("antrenorId", "Nu există antrenor cu ID-ul " + antrenorId);

        Antrenament antrenament = new Antrenament(0, denumire, data, durata, capacitate, antrenor);
        antrenamentDAO.save(antrenament);
    }

    public List<Antrenament> toateAntrenamentele() {
        return antrenamentDAO.findAll();
    }

    // Filtrare dupa data
    public List<Antrenament> antrenamenteDupaData(LocalDate data) {
        return antrenamentDAO.findByData(data);
    }

    // Filtrare dupa antrenor
    public List<Antrenament> antrenamenteDupaAntrenor(int antrenorId) {
        return antrenamentDAO.findAll().stream()
                .filter(a -> a.getAntrenor().getId() == antrenorId)
                .collect(Collectors.toList());
    }

    // Filtrare antrenamente cu locuri disponibile
    public List<Antrenament> antrenamenteCuLocuri() {
        return antrenamentDAO.findAll().stream()
                .filter(a -> a.locuriDisponibile() > 0)
                .collect(Collectors.toList());
    }

    // Cautare dupa denumire
    public List<Antrenament> cautaDupaDenumire(String denumire) {
        Validator.validareTextObligatoriu(denumire, "denumire");
        return antrenamentDAO.findAll().stream()
                .filter(a -> a.getDenumire().toLowerCase()
                        .contains(denumire.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void adaugaParticipant(int antrenamentId, int clientId) {
        Antrenament ant = antrenamentDAO.findAll().stream()
                .filter(a -> a.getId() == antrenamentId)
                .findFirst()
                .orElseThrow(() -> new ValidationException("antrenamentId",
                        "Nu există antrenament cu ID-ul " + antrenamentId));

        if (ant.locuriDisponibile() == 0)
            throw new ValidationException("capacitate", "Antrenamentul este complet.");

        Client client = clientDAO.findById(clientId);
        if (client == null)
            throw new ValidationException("clientId", "Nu există client cu ID-ul " + clientId);

        antrenamentDAO.addParticipant(antrenamentId, clientId);
    }

    public void stergeAntrenament(int id) {
        antrenamentDAO.delete(id);
    }

    public void actualizeazaAntrenament(int id, String denumire, LocalDateTime data,
                                        int durata, int capacitate, int antrenorId) {
        Validator.validareTextObligatoriu(denumire, "denumire");
        Validator.validareCapacitate(capacitate);
        Validator.validareDurata(durata);

        Antrenor antrenor = antrenorDAO.findById(antrenorId);
        if (antrenor == null)
            throw new ValidationException("antrenorId", "Nu există antrenor cu ID-ul " + antrenorId);

        Antrenament ant = new Antrenament(id, denumire, data, durata, capacitate, antrenor);
        antrenamentDAO.update(ant);
    }
}