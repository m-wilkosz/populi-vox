package com.populivox.backend.service;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.repository.WebsiteAdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(WebsiteAdminRepository websiteAdminRepository, PasswordEncoder passwordEncoder) {
        this.websiteAdminRepository = websiteAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public WebsiteAdmin register(RegistrationRequest request) {
        if (websiteAdminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }
        WebsiteAdmin websiteAdmin = new WebsiteAdmin();
        websiteAdmin.setEmail(request.getEmail());
        websiteAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

        return websiteAdminRepository.save(websiteAdmin);
    }
}