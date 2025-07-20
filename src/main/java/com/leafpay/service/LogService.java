package com.leafpay.service;

import com.leafpay.domain.Log;
import com.leafpay.repository.LogRepository;
import com.leafpay.service.dto.LogDTO;
import com.leafpay.service.mapper.LogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.leafpay.domain.Log}.
 */
@Service
@Transactional
public class LogService {

    private static final Logger LOG = LoggerFactory.getLogger(LogService.class);

    private final LogRepository logRepository;

    private final LogMapper logMapper;

    public LogService(LogRepository logRepository, LogMapper logMapper) {
        this.logRepository = logRepository;
        this.logMapper = logMapper;
    }

    /**
     * Save a log.
     *
     * @param logDTO the entity to save.
     * @return the persisted entity.
     */
    public LogDTO save(LogDTO logDTO) {
        LOG.debug("Request to save Log : {}", logDTO);
        Log log = logMapper.toEntity(logDTO);
        log = logRepository.save(log);
        return logMapper.toDto(log);
    }

    /**
     * Update a log.
     *
     * @param logDTO the entity to save.
     * @return the persisted entity.
     */
    public LogDTO update(LogDTO logDTO) {
        LOG.debug("Request to update Log : {}", logDTO);
        Log log = logMapper.toEntity(logDTO);
        log = logRepository.save(log);
        return logMapper.toDto(log);
    }

    /**
     * Partially update a log.
     *
     * @param logDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LogDTO> partialUpdate(LogDTO logDTO) {
        LOG.debug("Request to partially update Log : {}", logDTO);

        return logRepository
            .findById(logDTO.getId())
            .map(existingLog -> {
                logMapper.partialUpdate(existingLog, logDTO);

                return existingLog;
            })
            .map(logRepository::save)
            .map(logMapper::toDto);
    }

    /**
     * Get all the logs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Logs");
        return logRepository.findAll(pageable).map(logMapper::toDto);
    }

    /**
     * Get one log by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LogDTO> findOne(Long id) {
        LOG.debug("Request to get Log : {}", id);
        return logRepository.findById(id).map(logMapper::toDto);
    }

    /**
     * Delete the log by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Log : {}", id);
        logRepository.deleteById(id);
    }
}
