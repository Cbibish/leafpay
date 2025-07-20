package com.leafpay.service;

import com.leafpay.domain.AlerteSecurite;
import com.leafpay.repository.AlerteSecuriteRepository;
import com.leafpay.service.dto.AlerteSecuriteDTO;
import com.leafpay.service.mapper.AlerteSecuriteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.leafpay.domain.AlerteSecurite}.
 */
@Service
@Transactional
public class AlerteSecuriteService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteSecuriteService.class);

    private final AlerteSecuriteRepository alerteSecuriteRepository;

    private final AlerteSecuriteMapper alerteSecuriteMapper;

    public AlerteSecuriteService(AlerteSecuriteRepository alerteSecuriteRepository, AlerteSecuriteMapper alerteSecuriteMapper) {
        this.alerteSecuriteRepository = alerteSecuriteRepository;
        this.alerteSecuriteMapper = alerteSecuriteMapper;
    }

    /**
     * Save a alerteSecurite.
     *
     * @param alerteSecuriteDTO the entity to save.
     * @return the persisted entity.
     */
    public AlerteSecuriteDTO save(AlerteSecuriteDTO alerteSecuriteDTO) {
        LOG.debug("Request to save AlerteSecurite : {}", alerteSecuriteDTO);
        AlerteSecurite alerteSecurite = alerteSecuriteMapper.toEntity(alerteSecuriteDTO);
        alerteSecurite = alerteSecuriteRepository.save(alerteSecurite);
        return alerteSecuriteMapper.toDto(alerteSecurite);
    }

    /**
     * Update a alerteSecurite.
     *
     * @param alerteSecuriteDTO the entity to save.
     * @return the persisted entity.
     */
    public AlerteSecuriteDTO update(AlerteSecuriteDTO alerteSecuriteDTO) {
        LOG.debug("Request to update AlerteSecurite : {}", alerteSecuriteDTO);
        AlerteSecurite alerteSecurite = alerteSecuriteMapper.toEntity(alerteSecuriteDTO);
        alerteSecurite = alerteSecuriteRepository.save(alerteSecurite);
        return alerteSecuriteMapper.toDto(alerteSecurite);
    }

    /**
     * Partially update a alerteSecurite.
     *
     * @param alerteSecuriteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlerteSecuriteDTO> partialUpdate(AlerteSecuriteDTO alerteSecuriteDTO) {
        LOG.debug("Request to partially update AlerteSecurite : {}", alerteSecuriteDTO);

        return alerteSecuriteRepository
            .findById(alerteSecuriteDTO.getId())
            .map(existingAlerteSecurite -> {
                alerteSecuriteMapper.partialUpdate(existingAlerteSecurite, alerteSecuriteDTO);

                return existingAlerteSecurite;
            })
            .map(alerteSecuriteRepository::save)
            .map(alerteSecuriteMapper::toDto);
    }

    /**
     * Get all the alerteSecurites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlerteSecuriteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AlerteSecurites");
        return alerteSecuriteRepository.findAll(pageable).map(alerteSecuriteMapper::toDto);
    }

    /**
     * Get one alerteSecurite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlerteSecuriteDTO> findOne(Long id) {
        LOG.debug("Request to get AlerteSecurite : {}", id);
        return alerteSecuriteRepository.findById(id).map(alerteSecuriteMapper::toDto);
    }

    /**
     * Delete the alerteSecurite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlerteSecurite : {}", id);
        alerteSecuriteRepository.deleteById(id);
    }
}
