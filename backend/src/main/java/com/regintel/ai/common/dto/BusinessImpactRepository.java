package com.regintel.ai.common.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessImpactRepository

        extends JpaRepository<Business, Long> {
}
