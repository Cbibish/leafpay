package com.leafpay.service;

import com.leafpay.domain.Compte;
import com.leafpay.domain.TypeTransaction;
import com.leafpay.repository.CompteRepository;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.mapper.CompteMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leafpay.service.TransactionService;

/**
 * Service Implementation for managing {@link com.leafpay.domain.Compte}.
 */
@Service
@Transactional
public class CompteService {

    private static final Logger LOG = LoggerFactory.getLogger(CompteService.class);

    private final CompteRepository compteRepository;

    private final CompteMapper compteMapper;

    private final TransactionService transactionService;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper, TransactionService transactionService) {
    this.compteRepository = compteRepository;
    this.compteMapper = compteMapper;
    this.transactionService = transactionService;
}


    /**
     * Save a compte.
     *
     * @param compteDTO the entity to save.
     * @return the persisted entity.
     */
    public CompteDTO save(CompteDTO compteDTO) {
        LOG.debug("Request to save Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    /**
     * Update a compte.
     *
     * @param compteDTO the entity to save.
     * @return the persisted entity.
     */
    public CompteDTO update(CompteDTO compteDTO) {
        LOG.debug("Request to update Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    /**
     * Partially update a compte.
     *
     * @param compteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompteDTO> partialUpdate(CompteDTO compteDTO) {
        LOG.debug("Request to partially update Compte : {}", compteDTO);

        return compteRepository
            .findById(compteDTO.getId())
            .map(existingCompte -> {
                compteMapper.partialUpdate(existingCompte, compteDTO);

                return existingCompte;
            })
            .map(compteRepository::save)
            .map(compteMapper::toDto);
    }

    /**
     * Get all the comptes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Comptes");
        return compteRepository.findAll(pageable).map(compteMapper::toDto);
    }

    /**
     * Get one compte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompteDTO> findOne(Long id) {
        LOG.debug("Request to get Compte : {}", id);
        return compteRepository.findById(id).map(compteMapper::toDto);
    }

    /**
     * Delete the compte by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Compte : {}", id);
        compteRepository.deleteById(id);
    }

    public Optional<CompteDTO> deposit(Long compteId, BigDecimal amount) {
    return compteRepository.findById(compteId).map(compte -> {
        compte.setSolde(compte.getSolde().add(amount));
        compteRepository.save(compte);

        // Log transaction
        transactionService.logTransaction(compte, null, amount, "DEPOT", "User deposit");

        return compteMapper.toDto(compte);
    });
}

public Optional<CompteDTO> withdraw(Long compteId, BigDecimal amount) {
    return compteRepository.findById(compteId).filter(c -> c.getSolde().compareTo(amount) >= 0).map(compte -> {
        compte.setSolde(compte.getSolde().subtract(amount));
        compteRepository.save(compte);

        // Log transaction
        transactionService.logTransaction(compte, null, amount, "RETRAIT", "User withdrawal");

        return compteMapper.toDto(compte);
    });
}

}
