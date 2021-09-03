package com.kartaca.slcm.api.exception;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String entityName, Long id) {
        super("Could not find " + entityName + " with id " + id);
    }

    public NotFoundException() {
        super("Resource not found");
    }
}
