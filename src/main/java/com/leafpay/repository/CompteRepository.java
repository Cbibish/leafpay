package com.leafpay.repository;

import com.leafpay.domain.Compte;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Compte entity.
 */

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    List<Compte> findByDateFermetureIsNull();
}