package com.populivox.backend.dto;

import lombok.Data;

@Data
public class VerificationResponse {

    private String message;
    private boolean success;

    public VerificationResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}