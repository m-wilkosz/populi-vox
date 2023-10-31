package com.populivox.backend.service;

import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.repository.WebsiteAdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VerificationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    public VerificationService(WebsiteAdminRepository websiteAdminRepository) {
        this.websiteAdminRepository = websiteAdminRepository;
    }

    @Transactional
    public VerificationResponse verifyEmail(String token) {
        return websiteAdminRepository.findByEmailVerificationToken(token)
                .filter(admin -> !admin.getEmailVerificationExpiry().isBefore(LocalDateTime.now()))
                .map(admin -> {
                    admin.setEmailVerified(true);
                    websiteAdminRepository.save(admin);
                    return new VerificationResponse("Email successfully verified", true);
                })
                .orElseGet(() -> new VerificationResponse("Invalid token or token has expired", false));
    }
}