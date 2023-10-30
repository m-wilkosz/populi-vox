package com.populivox.backend.controller;

import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.service.VerificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-email")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public VerificationResponse verifyEmail(@RequestParam String token) {
        return verificationService.verifyEmail(token);
    }
}