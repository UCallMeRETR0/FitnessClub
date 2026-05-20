package com.fitnessclub.exceptions;

public class ValidationException extends RuntimeException {
    private final String camp;

    public ValidationException(String camp, String mesaj) {
        super("Câmp invalid [" + camp + "]: " + mesaj);
        this.camp = camp;
    }

    public String getCamp() { return camp; }
}
