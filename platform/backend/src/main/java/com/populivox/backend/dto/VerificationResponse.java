package com.populivox.backend.dto;

public class VerificationResponse {

    private String message;
    private boolean success;

    public VerificationResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}