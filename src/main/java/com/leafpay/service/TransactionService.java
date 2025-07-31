package com.leafpay.service;

import com.leafpay.domain.Compte;
import com.leafpay.domain.Transaction;
import com.leafpay.domain.TypeTransaction;
import com.leafpay.repository.CompteRepository;
import com.leafpay.repository.TransactionRepository;
import com.leafpay.service.dto.DepositRequestDTO;
import com.leafpay.service.dto.TransactionDTO;
import com.leafpay.service.dto.TransferRequestDTO;
import com.leafpay.service.dto.WithdrawalRequestDTO;
import com.leafpay.service.mapper.TransactionMapper;
import com.leafpay.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.leafpay.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private static final int MAX_MONTHLY_WITHDRAWALS_SAVINGS = 3; // or whatever limit you want

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CompteRepository compteRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper,
            CompteRepository compteRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.compteRepository = compteRepository;
    }

public void logTransaction(Compte source, Compte destination, BigDecimal amount, String type, String description) {
    Transaction transaction = new Transaction();
    transaction.setCompteSource(source);
    transaction.setCompteDestination(destination);
    transaction.setMontant(amount);
    transaction.setTypeTransaction(type);
    transaction.setLibelle(description);
    transaction.setStatut("COMPLETED");
    transaction.setDateTransaction(Instant.now());

    transactionRepository.save(transaction);
}


    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionDTO save(TransactionDTO transactionDTO) {
        LOG.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Update a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionDTO update(TransactionDTO transactionDTO) {
        LOG.debug("Request to update Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Partially update a transaction.
     *
     * @param transactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionDTO> partialUpdate(TransactionDTO transactionDTO) {
        LOG.debug("Request to partially update Transaction : {}", transactionDTO);

        return transactionRepository
                .findById(transactionDTO.getId())
                .map(existingTransaction -> {
                    transactionMapper.partialUpdate(existingTransaction, transactionDTO);

                    return existingTransaction;
                })
                .map(transactionRepository::save)
                .map(transactionMapper::toDto);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable).map(transactionMapper::toDto);
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findOne(Long id) {
        LOG.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id).map(transactionMapper::toDto);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    @Transactional
    public void transferMoney(TransferRequestDTO transferRequest) {
        Compte fromAccount = compteRepository.findById(transferRequest.getFromAccountId())
                .orElseThrow(() -> new BadRequestAlertException("Source account not found", "transaction",
                        "fromaccnotfound"));

        Compte toAccount = compteRepository.findById(transferRequest.getToAccountId())
                .orElseThrow(() -> new BadRequestAlertException("Destination account not found", "transaction",
                        "toaccnotfound"));

        BigDecimal amount = transferRequest.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestAlertException("Amount must be positive", "transaction", "invalidamount");
        }

        if (fromAccount.getSolde().compareTo(amount) < 0) {
            throw new BadRequestAlertException("Insufficient balance", "transaction", "insufficientbalance");
        }

        // Update balances
        fromAccount.setSolde(fromAccount.getSolde().subtract(amount));
        toAccount.setSolde(toAccount.getSolde().add(amount));

        compteRepository.save(fromAccount);
        compteRepository.save(toAccount);

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setMontant(amount);
        transaction.setTypeTransaction("TRANSFER");
        transaction.setDateTransaction(Instant.now());
        transaction.setStatut("COMPLETED");
        transaction.setCompteSource(fromAccount);
        transaction.setCompteDestination(toAccount);
        transaction.setJustificatif(transferRequest.getJustificatif());
        transaction.setMoyenValidation(transferRequest.getMoyenValidation());

        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> findByCompteId(Long compteId) {
    return transactionRepository.findByCompteId(compteId)
        .stream()
        .map(transactionMapper::toDto)
        .toList();
}
@Transactional
public void withdraw(WithdrawalRequestDTO withdrawalRequest) {
    Compte compte = compteRepository.findById(withdrawalRequest.getCompteId())
            .orElseThrow(() -> new BadRequestAlertException("Account not found", "transaction", "accnotfound"));

    BigDecimal amount = withdrawalRequest.getMontant();

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BadRequestAlertException("Amount must be positive", "transaction", "invalidamount");
    }

    // Check balance
    if (compte.getSolde().compareTo(amount) < 0) {
        throw new BadRequestAlertException("Insufficient balance", "transaction", "insufficientbalance");
    }

    // Check for withdrawal limits if savings account
    if ("SAVINGS".equalsIgnoreCase(compte.getTypeCompte())) {
        int withdrawalsThisMonth = countMonthlyWithdrawals(compte.getId());
        if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS_SAVINGS) {
            throw new BadRequestAlertException("Monthly withdrawal limit exceeded", "transaction", "withdrawallimitexceeded");
        }
    }

    // TODO: Check other account specific rules like no overdraft for student, etc.

    // Perform withdrawal
    compte.setSolde(compte.getSolde().subtract(amount));
    compteRepository.save(compte);

    Transaction transaction = new Transaction();
    transaction.setCompteSource(compte);
    transaction.setMontant(amount);
    transaction.setTypeTransaction(TypeTransaction.RETRAIT.name());
    transaction.setDateTransaction(Instant.now());
    transaction.setStatut("COMPLETED");

    transactionRepository.save(transaction);
}


@Transactional
public void deposit(DepositRequestDTO depositRequest) {
    Compte compte = compteRepository.findById(depositRequest.getCompteId())
            .orElseThrow(() -> new BadRequestAlertException("Account not found", "transaction", "accnotfound"));

    BigDecimal amount = depositRequest.getMontant();

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BadRequestAlertException("Amount must be positive", "transaction", "invalidamount");
    }

    compte.setSolde(compte.getSolde().add(amount));
    compteRepository.save(compte);

    Transaction transaction = new Transaction();
    transaction.setCompteDestination(compte);
    transaction.setMontant(amount);
    transaction.setTypeTransaction(TypeTransaction.DEPOSIT.name());
    transaction.setDateTransaction(Instant.now());
    transaction.setStatut("COMPLETED");

    transactionRepository.save(transaction);
}

@Transactional(readOnly = true)
public int countMonthlyWithdrawals(Long compteId) {
    Instant startOfMonth = LocalDateTime.now()
        .withDayOfMonth(1)
        .toInstant(ZoneOffset.UTC);

    return transactionRepository.countByCompteSourceIdAndTypeTransactionAndDateTransactionAfter(
        compteId, TypeTransaction.RETRAIT.name(), startOfMonth);
}


}
