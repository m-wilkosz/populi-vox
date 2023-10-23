package com.populivox.backend.model;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "websiteusers")
public class WebsiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User must be associated with a website")
    @ManyToOne
    @JoinTable(name = "websiteuser_website",
            joinColumns = @JoinColumn(name = "websiteuser_id"),
            inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Website associatedWebsite;
}