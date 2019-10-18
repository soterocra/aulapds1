package com.soterocra.aulapds1.dto;

import java.io.Serializable;

public class EmailDTO implements Serializable {
    private static final long serialVersionUID = -6127512051195762627L;

    private String email;

    public EmailDTO() {
    }

    public EmailDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
