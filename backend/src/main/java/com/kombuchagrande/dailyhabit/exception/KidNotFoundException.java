package com.kombuchagrande.dailyhabit.exception;

public class KidNotFoundException extends RuntimeException {
    public KidNotFoundException(String kid) {
        super("KID_NOT_FOUND: kid not found: " + kid);
    }
}