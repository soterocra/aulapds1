package com.soterocra.aulapds1.resources.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
    private static final long serialVersionUID = 36210003870433632L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError() {
    }

    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
