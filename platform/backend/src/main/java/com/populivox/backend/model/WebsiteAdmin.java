package com.populivox.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;

    @NotNull(message = "Admin must be associated with a website")
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