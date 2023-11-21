package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "websiteusers")
public class WebsiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User must be associated with a website")
    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website associatedWebsite;

    @OneToMany(mappedBy = "associatedUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Feedback> userFeedbacks;
}