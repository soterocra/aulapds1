package com.soterocra.aulapds1.services.exceptions;

public class JWTAuthorizationException extends RuntimeException {
    private static final long serialVersionUID = -9210364759917365223L;

    public JWTAuthorizationException(String msg) {
        super(msg);
    }
}
