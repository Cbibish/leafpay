package com.leafpay.repository;

import com.leafpay.domain.UtilisateurCompte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UtilisateurCompte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurCompteRepository extends JpaRepository<UtilisateurCompte, Long> {
    boolean existsByUtilisateurIdAndCompteIdAndRoleUtilisateurSurCeCompte(Long utilisateurId, Long compteId, String role);

    Page<UtilisateurCompte> findByUtilisateurId(Long utilisateurId, Pageable pageable);
}
