package com.populivox.backend.service;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.dto.RegistrationResponse;
import com.populivox.backend.exception.EmailAlreadyExistsException;
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

    public RegistrationResponse register(RegistrationRequest request) {
        if (websiteAdminRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        WebsiteAdmin websiteAdmin = new WebsiteAdmin();
        websiteAdmin.setEmail(request.getEmail());
        websiteAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

        websiteAdmin = websiteAdminRepository.save(websiteAdmin);
        return new RegistrationResponse(websiteAdmin.getEmail(), "Registration successful");
    }
}