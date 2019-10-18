package com.soterocra.aulapds1.services.exceptions;

public class JWTAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -9210364759917365223L;

    public JWTAuthenticationException(String msg) {
        super(msg);
    }
}
