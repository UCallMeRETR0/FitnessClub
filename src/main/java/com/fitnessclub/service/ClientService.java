package com.fitnessclub.service;

import com.fitnessclub.dao.AbonamentDAO;
import com.fitnessclub.dao.ClientDAO;
import com.fitnessclub.exceptions.AbonamentExpiratException;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService {

    private final ClientDAO clientDAO;
    private final AbonamentDAO abonamentDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
        this.abonamentDAO = new AbonamentDAO();
    }

    public void adaugaClient(String nume, String prenume, String email,
                             String telefon, LocalDate dataNasterii) {
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validareDataNasterii(dataNasterii);

        Client client = new Client(0, nume, prenume, email, telefon, dataNasterii);
        clientDAO.save(client);
    }

    public List<Client> totiClientii() {
        return clientDAO.findAll();
    }

    public Client gasesteDupaId(int id) {
        Client client = clientDAO.findById(id);
        if (client == null)
            throw new ValidationException("id", "Nu există client cu ID-ul " + id);
        return client;
    }

    public List<Client> cautaDupaNume(String nume) {
        Validator.validareTextObligatoriu(nume, "nume");
        return clientDAO.findByNume(nume);
    }

    public void actualizeazaClient(int id, String nume, String prenume,
                                   String email, String telefon, LocalDate dataNasterii) {
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validareDataNasterii(dataNasterii);

        Client client = gasesteDupaId(id);
        client.setNume(nume);
        client.setEmail(email);
        client.setTelefon(telefon);
        clientDAO.update(client);
    }

    public void stergeClient(int id) {
        gasesteDupaId(id); // verifica ca exista
        clientDAO.delete(id);
    }

    public boolean areAbonamentActiv(int clientId) {
        List<Abonament> abonamente = abonamentDAO.findByClientId(clientId);
        return abonamente.stream().anyMatch(Abonament::esteActiv);
    }

    public Abonament abonamentActiv(int clientId) {
        List<Abonament> abonamente = abonamentDAO.findByClientId(clientId);
        return abonamente.stream()
                .filter(Abonament::esteActiv)
                .findFirst()
                .orElseThrow(() -> new AbonamentExpiratException(clientId));
    }

    // Filtrare clienti care au abonament activ
    public List<Client> clientiCuAbonamentActiv() {
        return clientDAO.findAll().stream()
                .filter(c -> areAbonamentActiv(c.getId()))
                .collect(Collectors.toList());
    }

    // Filtrare clienti fara abonament activ
    public List<Client> clientiFaraAbonamentActiv() {
        return clientDAO.findAll().stream()
                .filter(c -> !areAbonamentActiv(c.getId()))
                .collect(Collectors.toList());
    }
}