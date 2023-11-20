package com.populivox.backend.repository;

import com.populivox.backend.model.WebsiteAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WebsiteAdminRepository extends JpaRepository<WebsiteAdmin, Long> {

    boolean existsByEmail(String email);

    Optional<WebsiteAdmin> findByEmailVerificationToken(String token);

    Optional<WebsiteAdmin> findByAssociatedWebsiteId(Long websiteId);
}