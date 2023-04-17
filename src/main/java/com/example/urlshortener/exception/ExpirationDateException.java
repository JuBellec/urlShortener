package com.example.urlshortener.exception;

public class ExpirationDateException extends Exception{
    public ExpirationDateException(String errorMessage) {
        super(errorMessage);
    }
}
