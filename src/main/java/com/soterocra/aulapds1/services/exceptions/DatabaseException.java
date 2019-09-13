package com.soterocra.aulapds1.services.exceptions;

public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = -9210364759917365223L;

    public DatabaseException(String msg) {
        super(msg);
    }
}
