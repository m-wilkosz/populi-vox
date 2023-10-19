package com.populivox.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "websites")
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "associatedWebsite")
    private List<User> users;

    @OneToMany(mappedBy = "website")
    private List<Feedback> feedbacks;
}