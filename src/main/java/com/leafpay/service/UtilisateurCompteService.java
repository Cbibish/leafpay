package com.leafpay.service;

import com.leafpay.domain.UtilisateurCompte;
import com.leafpay.repository.UtilisateurCompteRepository;
import com.leafpay.service.dto.UtilisateurCompteDTO;
import com.leafpay.service.mapper.UtilisateurCompteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.leafpay.domain.UtilisateurCompte}.
 */
@Service
@Transactional
public class UtilisateurCompteService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurCompteService.class);

    private final UtilisateurCompteRepository utilisateurCompteRepository;

    private final UtilisateurCompteMapper utilisateurCompteMapper;

    public UtilisateurCompteService(
        UtilisateurCompteRepository utilisateurCompteRepository,
        UtilisateurCompteMapper utilisateurCompteMapper
    ) {
        this.utilisateurCompteRepository = utilisateurCompteRepository;
        this.utilisateurCompteMapper = utilisateurCompteMapper;
    }

    /**
     * Save a utilisateurCompte.
     *
     * @param utilisateurCompteDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurCompteDTO save(UtilisateurCompteDTO utilisateurCompteDTO) {
        LOG.debug("Request to save UtilisateurCompte : {}", utilisateurCompteDTO);
        UtilisateurCompte utilisateurCompte = utilisateurCompteMapper.toEntity(utilisateurCompteDTO);
        utilisateurCompte = utilisateurCompteRepository.save(utilisateurCompte);
        return utilisateurCompteMapper.toDto(utilisateurCompte);
    }

    /**
     * Update a utilisateurCompte.
     *
     * @param utilisateurCompteDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurCompteDTO update(UtilisateurCompteDTO utilisateurCompteDTO) {
        LOG.debug("Request to update UtilisateurCompte : {}", utilisateurCompteDTO);
        UtilisateurCompte utilisateurCompte = utilisateurCompteMapper.toEntity(utilisateurCompteDTO);
        utilisateurCompte = utilisateurCompteRepository.save(utilisateurCompte);
        return utilisateurCompteMapper.toDto(utilisateurCompte);
    }

    /**
     * Partially update a utilisateurCompte.
     *
     * @param utilisateurCompteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtilisateurCompteDTO> partialUpdate(UtilisateurCompteDTO utilisateurCompteDTO) {
        LOG.debug("Request to partially update UtilisateurCompte : {}", utilisateurCompteDTO);

        return utilisateurCompteRepository
            .findById(utilisateurCompteDTO.getId())
            .map(existingUtilisateurCompte -> {
                utilisateurCompteMapper.partialUpdate(existingUtilisateurCompte, utilisateurCompteDTO);

                return existingUtilisateurCompte;
            })
            .map(utilisateurCompteRepository::save)
            .map(utilisateurCompteMapper::toDto);
    }

    /**
     * Get all the utilisateurComptes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UtilisateurCompteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UtilisateurComptes");
        return utilisateurCompteRepository.findAll(pageable).map(utilisateurCompteMapper::toDto);
    }

    /**
     * Get one utilisateurCompte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtilisateurCompteDTO> findOne(Long id) {
        LOG.debug("Request to get UtilisateurCompte : {}", id);
        return utilisateurCompteRepository.findById(id).map(utilisateurCompteMapper::toDto);
    }

    /**
     * Delete the utilisateurCompte by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UtilisateurCompte : {}", id);
        utilisateurCompteRepository.deleteById(id);
    }
}
