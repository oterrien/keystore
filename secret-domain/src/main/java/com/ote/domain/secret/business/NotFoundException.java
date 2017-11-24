package com.ote.domain.secret.business;

public class NotFoundException extends Exception {
    public NotFoundException(String name) {
        super("Secret with name '" + name + "' has not been found");
    }
}
