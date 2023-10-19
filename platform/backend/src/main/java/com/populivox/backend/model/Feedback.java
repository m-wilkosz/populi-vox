package com.populivox.backend.model;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL
    }
}