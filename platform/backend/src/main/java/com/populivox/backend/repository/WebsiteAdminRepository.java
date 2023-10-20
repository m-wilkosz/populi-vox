package com.populivox.backend.repository;

import com.populivox.backend.model.WebsiteAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteAdminRepository extends JpaRepository<WebsiteAdmin, Long> {

    boolean existsByEmail(String email);
}