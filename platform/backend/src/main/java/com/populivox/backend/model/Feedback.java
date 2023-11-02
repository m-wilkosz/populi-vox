package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summary;

    // conversation, topics, keywords

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    @NotNull(message = "Feedback must be associated with a user")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private WebsiteUser associatedUser;

    @NotNull(message = "Feedback must be associated with a website")
    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website associatedWebsite;

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL
    }
}