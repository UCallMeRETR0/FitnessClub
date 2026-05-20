package com.fitnessclub.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String mesaj) {
        super("Eroare BD: " + mesaj);
    }

    public DatabaseException(String mesaj, Throwable cauza) {
        super("Eroare BD: " + mesaj, cauza);
    }
}