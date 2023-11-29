package com.populivox.backend.controller;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.dto.RegistrationResponse;
import com.populivox.backend.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTests {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    private RegistrationRequest validRequest;

    private RegistrationResponse expectedResponse;

    @BeforeEach
    void setup() {
        validRequest = new RegistrationRequest(
                "user@email.com",
                "U7$c8iM*Hs4",
                "website");
        expectedResponse = new RegistrationResponse(
                "user@email.com",
                "Registration successful. Please check your email to verify your account.");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(registrationService.register(validRequest)).thenReturn(expectedResponse);

        RegistrationResponse response = registrationController.register(validRequest);

        assertEquals(expectedResponse, response);
        verify(registrationService).register(validRequest);
    }
}