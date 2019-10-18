package com.soterocra.aulapds1.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5825700880622442930L;

    public ResourceNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
