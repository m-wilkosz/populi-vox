package com.populivox.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerificationControllerTests {

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private VerificationController verificationController;

    @Test
    void testVerifyEmailSuccess() {
        String token = "valid-token";
        VerificationResponse mockResponse = new VerificationResponse(
                                            "Email successfully verified",
                                            true);
        when(verificationService.verifyEmail(token)).thenReturn(mockResponse);

        VerificationResponse response = verificationController.verifyEmail(token);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Email successfully verified", response.getMessage());
        verify(verificationService).verifyEmail(token);
    }

    @Test
    void testVerifyEmailFailure() {
        String token = "invalid-token";
        VerificationResponse mockResponse = new VerificationResponse(
                                            "Invalid token or token has expired",
                                            false);
        when(verificationService.verifyEmail(token)).thenReturn(mockResponse);

        VerificationResponse response = verificationController.verifyEmail(token);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid token or token has expired", response.getMessage());
        verify(verificationService).verifyEmail(token);
    }
}