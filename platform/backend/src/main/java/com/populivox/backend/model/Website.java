package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "websites")
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Website must have admins")
    @OneToMany(mappedBy = "associatedWebsite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WebsiteAdmin> websiteAdmins;

    @OneToMany(mappedBy = "associatedWebsite", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WebsiteUser> websiteUsers;

    @OneToMany(mappedBy = "associatedWebsite", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Feedback> websiteFeedbacks;
}