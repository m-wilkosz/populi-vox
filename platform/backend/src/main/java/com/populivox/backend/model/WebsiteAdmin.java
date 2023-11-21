package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "websiteadmins")
public class WebsiteAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;

    @NotNull
    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @NotNull
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @NotNull
    @Column(name = "email_verification_expiry")
    private LocalDateTime emailVerificationExpiry;

    @NotNull(message = "Admin must be associated with a website")
    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website associatedWebsite;
}