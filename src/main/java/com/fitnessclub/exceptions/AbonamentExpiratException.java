package com.fitnessclub.exceptions;

public class AbonamentExpiratException extends RuntimeException {

    public AbonamentExpiratException(int idClient) {
        super("Clientul cu ID " + idClient + " nu are un abonament activ.");
    }
}