package com.populivox.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.populivox.backend.repository.WebsiteAdminRepository;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.dto.VerificationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceTests {

    @Mock
    private WebsiteAdminRepository websiteAdminRepository;

    @InjectMocks
    private VerificationService verificationService;

    @Test
    public void testVerifyEmailWithValidToken() {
        String token = "validToken";
        WebsiteAdmin admin = new WebsiteAdmin();
        admin.setEmailVerificationExpiry(LocalDateTime.now().plusDays(1));
        when(websiteAdminRepository.findByEmailVerificationToken(token)).thenReturn(Optional.of(admin));

        VerificationResponse response = verificationService.verifyEmail(token);

        assertTrue(response.isSuccess());
        assertEquals("Email successfully verified", response.getMessage());
    }

    @Test
    public void testVerifyEmailWithInvalidToken() {
        String invalidToken = "invalidToken";
        when(websiteAdminRepository.findByEmailVerificationToken(invalidToken)).thenReturn(Optional.empty());

        VerificationResponse response = verificationService.verifyEmail(invalidToken);

        assertFalse(response.isSuccess());
        assertEquals("Invalid token or token has expired", response.getMessage());
    }

    @Test
    public void testVerifyEmailWithExpiredToken() {
        String expiredToken = "expiredToken";
        WebsiteAdmin expiredAdmin = new WebsiteAdmin();
        expiredAdmin.setEmailVerificationExpiry(LocalDateTime.now().minusDays(1));
        when(websiteAdminRepository.findByEmailVerificationToken(expiredToken)).thenReturn(Optional.of(expiredAdmin));

        VerificationResponse response = verificationService.verifyEmail(expiredToken);

        assertFalse(response.isSuccess());
        assertEquals("Invalid token or token has expired", response.getMessage());
    }

    @Test
    public void testVerifyEmailWithDataAccessException() {
        String token = "someToken";
        when(websiteAdminRepository.findByEmailVerificationToken(token))
                .thenThrow(new DataAccessException("Data access error") {});

        assertThrows(DataAccessException.class, () -> verificationService.verifyEmail(token));
    }

    @Test
    public void testVerifyEmailWithNullToken() {
        assertThrows(IllegalArgumentException.class, () -> verificationService.verifyEmail(null));
    }

    @Test
    public void testVerifyEmailWithEmptyToken() {
        VerificationResponse response = verificationService.verifyEmail("");

        assertFalse(response.isSuccess());
        assertEquals("Invalid token or token has expired", response.getMessage());
    }

    @Test
    public void testVerifyEmailWithSpecialCharactersToken() {
        String specialToken = "validToken$#@!";
        WebsiteAdmin admin = new WebsiteAdmin();
        admin.setEmailVerificationExpiry(LocalDateTime.now().plusDays(1));
        when(websiteAdminRepository.findByEmailVerificationToken(specialToken)).thenReturn(Optional.of(admin));

        VerificationResponse response = verificationService.verifyEmail(specialToken);

        assertTrue(response.isSuccess());
        assertEquals("Email successfully verified", response.getMessage());
    }

    @Test
    public void testVerifyEmailAtExactExpirationTime() {
        String boundaryToken = "boundaryToken";
        WebsiteAdmin adminAtBoundary = new WebsiteAdmin();
        adminAtBoundary.setEmailVerificationExpiry(LocalDateTime.now()); // Token expires right now
        when(websiteAdminRepository.findByEmailVerificationToken(boundaryToken))
                                    .thenReturn(Optional.of(adminAtBoundary));

        VerificationResponse response = verificationService.verifyEmail(boundaryToken);

        assertFalse(response.isSuccess());
        assertEquals("Invalid token or token has expired", response.getMessage());
    }
}