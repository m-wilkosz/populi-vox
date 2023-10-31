package com.populivox.backend.controller;

import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-email")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public VerificationResponse verifyEmail(@RequestParam String token) {
        logger.info("Received verification request with token: {}", token);
        VerificationResponse response = verificationService.verifyEmail(token);
        if (response.isSuccess()) {
            logger.info("Email verification successful for token: {}", token);
        } else {
            logger.warn("Email verification failed for token: {}", token);
        }
        return response;
    }
}