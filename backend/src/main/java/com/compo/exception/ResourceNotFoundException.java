package com.compo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con id " + id + " no fue encontrado");
    }

    public ResourceNotFoundException(String resource, String field, String value) {
        super(resource + " con " + field + " '" + value + "' no fue encontrado");
    }
}
