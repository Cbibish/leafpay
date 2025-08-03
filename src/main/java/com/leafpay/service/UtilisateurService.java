package com.leafpay.service;

import com.leafpay.domain.Compte;
import com.leafpay.domain.Role;
import com.leafpay.domain.Utilisateur;
import com.leafpay.domain.UtilisateurCompte;
import com.leafpay.repository.CompteRepository;
import com.leafpay.repository.RoleRepository;
import com.leafpay.repository.UtilisateurCompteRepository;
import com.leafpay.repository.UtilisateurRepository;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.dto.UtilisateurDTO;
import com.leafpay.service.mapper.UtilisateurMapper;
import com.leafpay.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.leafpay.domain.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final CompteRepository compteRepository;

    private final UtilisateurCompteRepository utilisateurCompteRepository;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            UtilisateurMapper utilisateurMapper,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            CompteRepository compteRepository,
            UtilisateurCompteRepository utilisateurCompteRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.compteRepository = compteRepository;
        this.utilisateurCompteRepository = utilisateurCompteRepository;

    }

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
    // 1. Convert DTO to entity (usual JHipster mapping)
    Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);

    // 2. Encode password before saving user
    utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));

    // 3. Set creation date if new
    if (utilisateur.getDateCreation() == null) {
        utilisateur.setDateCreation(Instant.now());
    }

    // 4. Save the user entity
    utilisateur = utilisateurRepository.save(utilisateur);

    // 5. Process comptes from DTO and create Compte + UtilisateurCompte for each
    if (utilisateurDTO.getComptes() != null && !utilisateurDTO.getComptes().isEmpty()) {
        for (CompteDTO compteDTO : utilisateurDTO.getComptes()) {
            Compte compte = new Compte();
            compte.setTypeCompte(compteDTO.getTypeCompte());
            compte.setSolde(compteDTO.getSolde() != null ? compteDTO.getSolde() : BigDecimal.ZERO);
            compte.setPlafondTransaction(compteDTO.getPlafondTransaction());
            compte.setLimiteRetraitsMensuels(compteDTO.getLimiteRetraitsMensuels());
            compte.setTauxInteret(compteDTO.getTauxInteret());
            compte.setDateOuverture(compteDTO.getDateOuverture() != null ? compteDTO.getDateOuverture() : Instant.now());
            compte.setDateFermeture(compteDTO.getDateFermeture());
            compte.setStatut(compteDTO.getStatut());
            compte.setIban(compteDTO.getIban());

            // Save Compte
            compte = compteRepository.save(compte);

            // Create linking UtilisateurCompte
            UtilisateurCompte utilisateurCompte = new UtilisateurCompte();
            utilisateurCompte.setUtilisateur(utilisateur);
            utilisateurCompte.setCompte(compte);
            utilisateurCompte.setRoleUtilisateurSurCeCompte("Owner"); // default role on creation
            utilisateurCompteRepository.save(utilisateurCompte);
        }
    }

    // 6. Convert back to DTO to return
    return utilisateurMapper.toDto(utilisateur);
}


    /**
     * Update a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */

    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
    LOG.debug("Request to update Utilisateur : {}", utilisateurDTO);
    Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);

    // Encode password if present
    if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
    }

    if (utilisateurDTO.getRole() != null) {
        Role role = null;
        if (utilisateurDTO.getRole().getId() != null) {
            role = roleRepository.findById(utilisateurDTO.getRole().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID: " + utilisateurDTO.getRole().getId()));
        } else if (utilisateurDTO.getRole().getNom() != null) {
            role = roleRepository.findByNom(utilisateurDTO.getRole().getNom())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Role Name: " + utilisateurDTO.getRole().getNom()));
        }

        if (role != null) {
            utilisateur.setRole(role);
        }
    }

    utilisateur = utilisateurRepository.save(utilisateur);
    return utilisateurMapper.toDto(utilisateur);
}

    /**
     * Partially update a utilisateur.
     *
     * @param utilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to partially update Utilisateur : {}", utilisateurDTO);

        return utilisateurRepository
                .findById(utilisateurDTO.getId())
                .map(existingUtilisateur -> {
                    utilisateurMapper.partialUpdate(existingUtilisateur, utilisateurDTO);

                    if (utilisateurDTO.getRole() != null && utilisateurDTO.getRole().getId() != null) {
                        Role role = roleRepository
                                .findById(utilisateurDTO.getRole().getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Invalid Role ID: " + utilisateurDTO.getRole().getId()));
                        existingUtilisateur.setRole(role);
                    }

                    return existingUtilisateur;
                })
                .map(utilisateurRepository::save)
                .map(utilisateurMapper::toDto);
    }

    /**
     * Get all the utilisateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UtilisateurDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll(pageable).map(utilisateurMapper::toDto);
    }

    /**
     * Get one utilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id).map(utilisateurMapper::toDto);
    }

    /**
     * Delete the utilisateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurDTO> findOneByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .map(utilisateurMapper::toDto);
    }
}
