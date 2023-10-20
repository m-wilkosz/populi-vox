package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "websiteadmins")
public class WebsiteAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @ManyToOne
    @JoinTable(name = "websiteadmin_website",
            joinColumns = @JoinColumn(name = "websiteadmin_id"),
            inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Website associatedWebsite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WebsiteAdmin that = (WebsiteAdmin) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}