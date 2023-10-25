package com.populivox.backend.service;

import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.repository.WebsiteAdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    public VerificationService(WebsiteAdminRepository websiteAdminRepository) {
        this.websiteAdminRepository = websiteAdminRepository;
    }

    @Transactional
    public VerificationResponse verifyEmail(String token) {
        // Find the user by token
        Optional<WebsiteAdmin> websiteAdminOptional = websiteAdminRepository.findByEmailVerificationToken(token);

        if (!websiteAdminOptional.isPresent()) {
            return new VerificationResponse("Invalid token", false);
        }

        WebsiteAdmin websiteAdmin = websiteAdminOptional.get();

        // Check if the token is expired
        if (websiteAdmin.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            return new VerificationResponse("Token has expired", false);
        }

        // Set emailVerified to true
        websiteAdmin.setEmailVerified(true);
        websiteAdminRepository.save(websiteAdmin);

        return new VerificationResponse("Email successfully verified", true);
    }
}