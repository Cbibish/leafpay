package com.leafpay.repository;

import com.leafpay.domain.Log;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Log entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {}
