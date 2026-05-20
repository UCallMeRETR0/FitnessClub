package com.fitnessclub.util;

import com.fitnessclub.exceptions.ValidationException;
import java.time.LocalDate;

public class Validator {

    public static void validareTextObligatoriu(String valoare, String numeCamp) {
        if (valoare == null || valoare.trim().isEmpty()) {
            throw new ValidationException(numeCamp, "câmpul nu poate fi gol.");
        }
    }

    public static void validareEmail(String email) {
        if (email == null || !email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("email", "formatul emailului este incorect: " + email);
        }
    }

    public static void validareTelefon(String telefon) {
        if (telefon == null || !telefon.matches("^07[0-9]{8}$")) {
            throw new ValidationException("telefon", "telefonul trebuie să fie de forma 07XXXXXXXX.");
        }
    }

    public static void validarePret(double valoare, String numeCamp) {
        if (valoare <= 0) {
            throw new ValidationException(numeCamp, "valoarea trebuie să fie pozitivă, primit: " + valoare);
        }
    }

    public static void validareDataNasterii(LocalDate data) {
        if (data == null) {
            throw new ValidationException("dataNasterii", "data nu poate fi nulă.");
        }
        if (data.isAfter(LocalDate.now())) {
            throw new ValidationException("dataNasterii", "data nașterii nu poate fi în viitor.");
        }
    }

    public static void validareDataStart(LocalDate data) {
        if (data == null) {
            throw new ValidationException("dataStart", "data de start nu poate fi nulă.");
        }
        if (data.isAfter(LocalDate.now().plusDays(365))) {
            throw new ValidationException("dataStart", "data de start nu poate fi mai mult de un an în viitor.");
        }
    }

    public static void validareCapacitate(int capacitate) {
        if (capacitate <= 0) {
            throw new ValidationException("capacitate", "capacitatea trebuie să fie cel puțin 1.");
        }
        if (capacitate > 100) {
            throw new ValidationException("capacitate", "capacitatea nu poate depăși 100.");
        }
    }

    public static void validareDurata(int durata) {
        if (durata <= 0) {
            throw new ValidationException("durata", "durata trebuie să fie pozitivă.");
        }
    }
}