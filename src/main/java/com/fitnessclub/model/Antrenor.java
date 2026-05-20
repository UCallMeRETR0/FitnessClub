package com.fitnessclub.model;

import com.fitnessclub.enums.Specialitate;
import com.fitnessclub.exceptions.ValidationException;
import com.fitnessclub.util.Validator;

public class Antrenor extends Persoana {
    private Specialitate specialitate;
    private double salariu;

    public Antrenor(int id, String nume, String prenume, String email, String telefon,
                    Specialitate specialitate, double salariu) {
        super(id, nume, prenume, email, telefon);
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validarePret(salariu, "salariu");
        if (specialitate == null)
            throw new ValidationException("specialitate", "specialitatea nu poate fi nulă.");

        this.specialitate = specialitate;
        this.salariu = salariu;
    }

    public Specialitate getSpecialitate() { return specialitate; }
    public double getSalariu() { return salariu; }
    public void setSalariu(double salariu) {
        Validator.validarePret(salariu, "salariu");
        this.salariu = salariu;
    }

    @Override
    public String afisare() {
        return "Antrenor: " + prenume + " " + nume + " | Specialitate: " + specialitate;
    }
}