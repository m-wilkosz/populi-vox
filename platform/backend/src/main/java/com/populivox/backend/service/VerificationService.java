package com.populivox.backend.service;

import com.populivox.backend.dto.VerificationResponse;
import com.populivox.backend.repository.WebsiteAdminRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.populivox.backend.model.WebsiteAdmin;

@Service
public class VerificationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    public VerificationService(WebsiteAdminRepository websiteAdminRepository) {
        this.websiteAdminRepository = websiteAdminRepository;
    }

    /**
     * Verifies a user's email based on the provided token. This method is transactional,
     * ensuring that all operations within it are completed atomically.
     *
     * <p>The verification process involves the following steps:
     * <ul>
     *     <li>Searching for a {@link WebsiteAdmin} based on the provided email verification token.</li>
     *     <li>Checking if the token is still valid (i.e., not expired).</li>
     *     <li>Updating the admin's email verification status to true and saving this update if the token is valid.</li>
     *     <li>Returning a success response if the token is valid and the email is verified.</li>
     *     <li>Returning a failure response if the token is invalid or expired.</li>
     * </ul>
     *
     * @param token The email verification token sent to the user. It is used to identify the {@link WebsiteAdmin}
     *              and verify their email.
     * @return A {@link VerificationResponse} indicating the outcome of the verification process.
     *         It includes a success flag and a message. The message will state "Email successfully verified"
     *         if the process is successful, or "Invalid token or token has expired" if the token is invalid or expired.
     * @throws DataAccessException if there is an issue accessing the data layer during the process.
     */
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