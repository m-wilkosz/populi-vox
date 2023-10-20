package com.populivox.backend.controller;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.service.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public WebsiteAdmin register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
}