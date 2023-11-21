package com.populivox.backend.controller;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.dto.RegistrationResponse;
import com.populivox.backend.service.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * This endpoint handles the registration of a new user and associated website.
     * This method receives a registration request, processes it, and returns a registration response.
     *
     * @param request The {@link RegistrationRequest} object containing the necessary data
     *                for registration, such as email, password, and website name.
     *                This object should be provided in the body of the POST request.
     *                It is validated to ensure that all required fields are present and correctly formatted.
     * @return Returns a {@link RegistrationResponse} that contains the registration details
     *         such as the email address and a message indicating the success of the registration process.
     */
    @PostMapping
    public RegistrationResponse register(@RequestBody @Valid RegistrationRequest request) {
        return registrationService.register(request);
    }
}