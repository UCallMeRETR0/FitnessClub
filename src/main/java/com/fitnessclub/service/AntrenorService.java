package com.fitnessclub.service;

import com.fitnessclub.dao.AntrenorDAO;
import com.fitnessclub.enums.Specialitate;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.model.Antrenor;
import com.fitnessclub.util.Validator;

import java.util.List;

public class AntrenorService {

    private final AntrenorDAO antrenorDAO;

    public AntrenorService() {
        this.antrenorDAO = new AntrenorDAO();
    }

    public void adaugaAntrenor(String nume, String prenume, String email,
                               String telefon, Specialitate specialitate, double salariu) {
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validarePret(salariu, "salariu");

        Antrenor antrenor = new Antrenor(0, nume, prenume, email, telefon, specialitate, salariu);
        antrenorDAO.save(antrenor);
    }

    public List<Antrenor> totiAntrenorii() {
        return antrenorDAO.findAll();
    }

    public Antrenor gasesteDupaId(int id) {
        Antrenor antrenor = antrenorDAO.findById(id);
        if (antrenor == null)
            throw new ValidationException("id", "Nu există antrenor cu ID-ul " + id);
        return antrenor;
    }

    public List<Antrenor> filtreazaDupaSpecialitate(Specialitate specialitate) {
        return antrenorDAO.findBySpecialitate(specialitate);
    }

    public void actualizeazaAntrenor(int id, String nume, String prenume,
                                     String email, String telefon,
                                     Specialitate specialitate, double salariu) {
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validarePret(salariu, "salariu");

        Antrenor antrenor = new Antrenor(id, nume, prenume, email, telefon, specialitate, salariu);
        antrenorDAO.update(antrenor);
    }

    public void stergeAntrenor(int id) {
        gasesteDupaId(id);
        antrenorDAO.delete(id);
    }
}