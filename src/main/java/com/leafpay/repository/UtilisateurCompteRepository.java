package com.leafpay.repository;

import com.leafpay.domain.UtilisateurCompte;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UtilisateurCompte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurCompteRepository extends JpaRepository<UtilisateurCompte, Long> {
    boolean existsByUtilisateurIdAndCompteIdAndRoleUtilisateurSurCeCompte(Long utilisateurId, Long compteId,
            String role);

    @Query("SELECT uc FROM UtilisateurCompte uc " +
            "JOIN FETCH uc.compte " +
            "JOIN FETCH uc.utilisateur " +
            "WHERE uc.utilisateur.id = :utilisateurId")
    List<UtilisateurCompte> findByUtilisateurIdWithCompte(@Param("utilisateurId") Long utilisateurId);

    Page<UtilisateurCompte> findByUtilisateurId(Long utilisateurId, Pageable pageable);
}
