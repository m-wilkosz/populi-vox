package com.populivox.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "websiteusers")
public class WebsiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinTable(name = "websiteuser_website",
            joinColumns = @JoinColumn(name = "websiteuser_id"),
            inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Website associatedWebsite;
}