package com.fitnessclub.model;

import com.fitnessclub.util.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client extends Persoana {
    private LocalDate dataNasterii;
    private List<Abonament> abonamente;

    public Client(int id, String nume, String prenume, String email, String telefon, LocalDate dataNasterii) {
        super(id, nume, prenume, email, telefon);
        Validator.validareTextObligatoriu(nume, "nume");
        Validator.validareTextObligatoriu(prenume, "prenume");
        Validator.validareEmail(email);
        Validator.validareTelefon(telefon);
        Validator.validareDataNasterii(dataNasterii);

        this.dataNasterii = dataNasterii;
        this.abonamente = new ArrayList<>();
    }

    public void adaugaAbonament(Abonament a) {
        abonamente.add(a);
    }

    public List<Abonament> getAbonamente() { return abonamente; }
    public LocalDate getDataNasterii() { return dataNasterii; }

    @Override
    public String afisare() {
        return "Client: " + prenume + " " + nume + " | Email: " + email + " | Tel: " + telefon;
    }
}