package com.fitnessclub.service;

import com.fitnessclub.dao.AbonamentDAO;
import com.fitnessclub.dao.ClientDAO;
import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbonamentService {

    private final AbonamentDAO abonamentDAO;
    private final ClientDAO clientDAO;

    public AbonamentService() {
        this.abonamentDAO = new AbonamentDAO();
        this.clientDAO = new ClientDAO();
    }

    public void adaugaAbonament(int clientId, TipAbonament tip,
                                LocalDate dataStart, double pret) {
        Validator.validarePret(pret, "pret");
        Validator.validareDataStart(dataStart);

        Client client = clientDAO.findById(clientId);
        if (client == null)
            throw new ValidationException("clientId", "Nu există client cu ID-ul " + clientId);

        Abonament abonament = new Abonament(0, tip, dataStart, pret, client);
        abonamentDAO.save(abonament);
    }

    public List<Abonament> toateAbonamentele() {
        return abonamentDAO.findAll();
    }

    public List<Abonament> abonamenteDupaClient(int clientId) {
        return abonamentDAO.findByClientId(clientId);
    }

    // Filtrare dupa status
    public List<Abonament> abonamenteDupaStatus(StatusAbonament status) {
        return abonamentDAO.findByStatus(status);
    }

    // Filtrare dupa tip
    public List<Abonament> abonamenteDupaTip(TipAbonament tip) {
        return abonamentDAO.findAll().stream()
                .filter(a -> a.getTip() == tip)
                .collect(Collectors.toList());
    }

    // Filtrare abonamente care expira in urmatoarele N zile
    public List<Abonament> abonamenteCareExpiraCurand(int zile) {
        LocalDate limita = LocalDate.now().plusDays(zile);
        return abonamentDAO.findAll().stream()
                .filter(a -> a.esteActiv() && a.getDataExpirare().isBefore(limita))
                .collect(Collectors.toList());
    }

    public void actualizeazaStatus(int id, StatusAbonament status) {
        Abonament ab = abonamentDAO.findAll().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("id", "Nu există abonament cu ID-ul " + id));
        ab.setStatus(status);
        abonamentDAO.update(ab);
    }

    public void stergeAbonament(int id) {
        abonamentDAO.delete(id);
    }

    // Venituri totale
    public double venitriTotale() {
        return abonamentDAO.findAll().stream()
                .mapToDouble(Abonament::getPret)
                .sum();
    }

    // Venituri grupate pe tip
    public Map<TipAbonament, Double> venituriPeTip() {
        return abonamentDAO.findAll().stream()
                .collect(Collectors.groupingBy(
                        Abonament::getTip,
                        Collectors.summingDouble(Abonament::getPret)
                ));
    }
}