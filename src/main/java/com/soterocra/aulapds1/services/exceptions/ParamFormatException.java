package com.soterocra.aulapds1.services.exceptions;

public class ParamFormatException extends RuntimeException {
    private static final long serialVersionUID = -9210364759917365223L;

    public ParamFormatException(String msg) {
        super(msg);
    }
}
