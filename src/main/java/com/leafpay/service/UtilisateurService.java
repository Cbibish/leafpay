package com.leafpay.service;

import com.leafpay.domain.Role;
import com.leafpay.domain.Utilisateur;
import com.leafpay.repository.RoleRepository;
import com.leafpay.repository.UtilisateurRepository;
import com.leafpay.service.dto.UtilisateurDTO;
import com.leafpay.service.mapper.UtilisateurMapper;
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

    public UtilisateurService(
        UtilisateurRepository utilisateurRepository,
        UtilisateurMapper utilisateurMapper,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to save Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);

        // Encode password if it's not null or empty
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        }

        if (utilisateurDTO.getRole() != null && utilisateurDTO.getRole().getNom() != null) {
            Role role = roleRepository
                .findByNom(utilisateurDTO.getRole().getNom())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role Name: " + utilisateurDTO.getRole().getNom()));
            utilisateur.setRole(role);
        } else {
            throw new IllegalArgumentException("Role must be provided when registering a user.");
        }

        utilisateur = utilisateurRepository.save(utilisateur);
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

        if (utilisateurDTO.getRole() != null && utilisateurDTO.getRole().getId() != null) {
            Role role = roleRepository
                .findById(utilisateurDTO.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID: " + utilisateurDTO.getRole().getId()));
            utilisateur.setRole(role);
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
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID: " + utilisateurDTO.getRole().getId()));
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
}
