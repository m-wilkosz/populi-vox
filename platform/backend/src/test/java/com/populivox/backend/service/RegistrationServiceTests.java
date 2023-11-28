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
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Captor;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {

    @Mock
    private WebsiteAdminRepository websiteAdminRepository;

    @Mock
    private WebsiteRepository websiteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private RegistrationService registrationService;

    @Captor
    private ArgumentCaptor<WebsiteAdmin> adminCaptor;

    @BeforeEach
    public void setup() {
        WebsiteAdmin mockAdmin = new WebsiteAdmin();
        mockAdmin.setEmail("newemail@example.com");

        // Use lenient for stubs not used in every test
        lenient().when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        lenient().when(templateEngine.process(eq("verificationEmail.html"), any(Context.class)))
                .thenReturn("Mock Email Content");
        lenient().when(websiteAdminRepository.save(any(WebsiteAdmin.class))).thenReturn(mockAdmin);

        ReflectionTestUtils.setField(registrationService, "baseUrl", "http://example.com");
        ReflectionTestUtils.setField(registrationService, "senderAddress", "noreply@example.com");
    }

    @Test
    public void whenRegisterNewEmail_thenSuccess() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                                        "newemail@example.com",
                                        "password",
                                        "websiteName");
        mockSuccessfulRegistration();

        RegistrationResponse response = registrationService.register(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("newemail@example.com", response.getEmail()),
                () -> verify(websiteAdminRepository).save(adminCaptor.capture()),
                () -> assertEquals("newemail@example.com", adminCaptor.getValue().getEmail()),
                () -> verify(websiteRepository).save(any(Website.class)),
                () -> verify(mailSender).send(any(MimeMessage.class))
        );
    }

    @Test
    public void whenRegisterExistingEmail_thenThrowEmailAlreadyExistsException() {
        RegistrationRequest request = new RegistrationRequest(
                                        "existingemail@example.com",
                                        "password",
                                        "websiteName");
        when(websiteAdminRepository.existsByEmail("existingemail@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> registrationService.register(request));
    }

    @Test
    public void whenEmailSendingFails_thenThrowEmailSendingFailedException() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                                        "newemail@example.com",
                                        "password",
                                        "websiteName");
        mockSuccessfulRegistration();
        doThrow(new EmailSendingFailedException("Error", new Throwable()))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendingFailedException.class, () -> registrationService.register(request));
    }

    @Test
    public void whenRegisterWithNullRequest_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> registrationService.register(null));
    }

    @Test
    public void whenMessagingExceptionInEmailSending_thenThrowEmailSendingFailedException() {
        RegistrationRequest request = new RegistrationRequest(
                                            "validemail@example.com",
                                            "password",
                                            "websiteName");
        mockSuccessfulRegistration();

        doAnswer(invocation -> {
            throw new MessagingException("Mock Messaging Exception");
        }).when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendingFailedException.class, () -> registrationService.register(request));
    }

    @Test
    public void whenRegister_thenGenerateValidTokenAndExpiry() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                                            "validemail@example.com",
                                            "password",
                                            "websiteName");
        mockSuccessfulRegistration();

        RegistrationResponse response = registrationService.register(request);

        verify(websiteAdminRepository).save(adminCaptor.capture());
        WebsiteAdmin savedAdmin = adminCaptor.getValue();

        assertAll(
                () -> assertNotNull(savedAdmin.getEmailVerificationToken()),
                () -> assertTrue(savedAdmin.getEmailVerificationExpiry().isAfter(LocalDateTime.now()))
        );
    }

    private void mockSuccessfulRegistration() {
        when(websiteAdminRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }
}