package com.populivox.backend.service;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.dto.RegistrationResponse;
import com.populivox.backend.exception.EmailAlreadyExistsException;
import com.populivox.backend.exception.EmailSendingFailedException;
import com.populivox.backend.model.Website;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.repository.WebsiteAdminRepository;
import com.populivox.backend.repository.WebsiteRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

@Service
public class RegistrationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    private final WebsiteRepository websiteRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderAddress;

    @Value("${spring.baseUrl}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    public RegistrationService(WebsiteAdminRepository websiteAdminRepository,
                               WebsiteRepository websiteRepository,
                               PasswordEncoder passwordEncoder,
                               JavaMailSender mailSender,
                               TemplateEngine templateEngine) {
        this.websiteAdminRepository = websiteAdminRepository;
        this.websiteRepository = websiteRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Registers a new website and its admin based on the given registration request.
     * This method encapsulates the business logic for registering a new website admin
     * along with the creation of the associated website entity.
     *
     * <p>Steps involved in the registration process include:
     * <ul>
     *     <li>Validating the uniqueness of the admin's email.</li>
     *     <li>Creating and persisting a new {@link WebsiteAdmin} entity.</li>
     *     <li>Setting up a new {@link Website} entity associated with the admin.</li>
     *     <li>Generating an email verification token.</li>
     *     <li>Sending an email for verification purposes.</li>
     * </ul>
     *
     * @param request The {@link RegistrationRequest} containing necessary information
     *                such as admin's email, password, and website name for registration.
     * @return A {@link RegistrationResponse} that contains the email of the registered admin
     *         and a message indicating the successful completion of the registration process.
     * @throws EmailAlreadyExistsException if an admin with the given email already exists.
     * @throws EmailSendingFailedException if there is an issue with sending the verification email.
     */
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        // Check if email already exists
        if (websiteAdminRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Create website's admin instance
        WebsiteAdmin websiteAdmin = new WebsiteAdmin();
        websiteAdmin.setEmail(request.getEmail());
        websiteAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

        // Create website instance
        Website website = new Website();
        website.setName(request.getWebsiteName());

        // Create relationship between website and website's admin
        websiteAdmin.setAssociatedWebsite(website);
        website.setWebsiteAdmins(List.of(websiteAdmin));

        website = websiteRepository.save(website);

        // Generate email verification token
        String token = UUID.randomUUID().toString();
        websiteAdmin.setEmailVerificationToken(token);
        websiteAdmin.setEmailVerificationExpiry(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours

        websiteAdmin = websiteAdminRepository.save(websiteAdmin);

        // Send verification email
        sendVerificationEmail(websiteAdmin.getEmail(), token);

        return new RegistrationResponse(websiteAdmin.getEmail(),
                "Registration successful. Please check your email to verify your account.");
    }

    private void sendVerificationEmail(String email, String token) throws EmailSendingFailedException {
        try {
            // Create verification URL
            String verificationUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/verify-email")
                    .queryParam("token", token)
                    .toUriString();

            // Prepare the evaluation context
            final Context ctx = new Context();
            ctx.setVariable("verificationUrl", verificationUrl);

            // Create the HTML body using Thymeleaf
            final String htmlContent = this.templateEngine.process("verificationEmail.html", ctx);

            // Send email
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
            helper.setTo(email);
            helper.setFrom(senderAddress);
            helper.setSubject("Verify your email - PopuliVox");
            helper.setText(htmlContent, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            logger.error("Error sending verification email to {}: {}", email, e.getMessage());
            throw new EmailSendingFailedException("Error sending verification email to " + email, e);
            // TODO Notify an administrator, or even retry sending the email
        }
    }
}