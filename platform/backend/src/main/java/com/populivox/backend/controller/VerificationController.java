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

    /**
     * Handles the email verification process.
     * This method receives a verification token as a request parameter, processes it,
     * and returns a {@link VerificationResponse}.
     *
     * <p>The process involves the following steps:
     * <ul>
     *     <li>Receiving a verification token sent to the user's email.</li>
     *     <li>Calling the verification service to validate the token.</li>
     *     <li>Logging the outcome of the verification process.</li>
     * </ul>
     *
     * @param token The verification token received from the user.
     *              This token is passed as a request parameter and is used to verify the user's email.
     * @return A {@link VerificationResponse} that contains the result of the verification process,
     *         including a success flag and a message detailing the outcome.
     *         The response indicates whether the email verification was successful or not.
     */
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