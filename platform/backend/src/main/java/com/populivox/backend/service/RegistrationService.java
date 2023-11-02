package com.populivox.backend.service;

import com.populivox.backend.dto.RegistrationRequest;
import com.populivox.backend.dto.RegistrationResponse;
import com.populivox.backend.exception.EmailAlreadyExistsException;
import com.populivox.backend.exception.EmailSendingFailedException;
import com.populivox.backend.model.WebsiteAdmin;
import com.populivox.backend.repository.WebsiteAdminRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

@Service
public class RegistrationService {

    private final WebsiteAdminRepository websiteAdminRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderAddress;

    @Value("${spring.baseUrl}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    public RegistrationService(WebsiteAdminRepository websiteAdminRepository,
                               PasswordEncoder passwordEncoder,
                               JavaMailSender mailSender,
                               TemplateEngine templateEngine) {
        this.websiteAdminRepository = websiteAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        // Check if email already exists
        if (websiteAdminRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        WebsiteAdmin websiteAdmin = new WebsiteAdmin();
        websiteAdmin.setEmail(request.getEmail());
        websiteAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

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
            String verificationUrl = baseUrl + "/verify-email?token=" + token;

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