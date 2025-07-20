package com.leafpay.repository;

import com.leafpay.domain.AlerteSecurite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlerteSecurite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteSecuriteRepository extends JpaRepository<AlerteSecurite, Long> {}
