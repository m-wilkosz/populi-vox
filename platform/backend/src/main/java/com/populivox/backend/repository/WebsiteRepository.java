package com.populivox.backend.repository;

import com.populivox.backend.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<Website, Long> {

    boolean existsByName(String name);
}