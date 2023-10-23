package com.populivox.backend.model;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "websites")
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Website must have admins")
    @OneToMany(mappedBy = "associatedWebsite")
    private List<WebsiteAdmin> websiteAdmins;

    @NotNull(message = "Website must have users")
    @OneToMany(mappedBy = "associatedWebsite")
    private List<WebsiteUser> websiteUsers;

    @OneToMany(mappedBy = "website")
    private List<Feedback> feedbacks;
}