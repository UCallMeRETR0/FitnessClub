package com.fitnessclub.model;

public abstract class Persoana {
    protected int id;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String telefon;

    public Persoana(int id, String nume, String prenume, String email, String telefon) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
    }

    public abstract String afisare();

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }

    public void setNume(String nume) { this.nume = nume; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
}