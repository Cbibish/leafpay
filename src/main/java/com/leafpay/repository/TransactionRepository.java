package com.leafpay.repository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import com.leafpay.domain.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transaction entity.
 */


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Transactions where the account is source or destination
    @Query("SELECT t FROM Transaction t WHERE t.compteSource.id = :compteId OR t.compteDestination.id = :compteId")
    List<Transaction> findByCompteId(@Param("compteId") Long compteId);

    // If you want only outgoing (source)
    List<Transaction> findByCompteSource_Id(Long compteId);

    int countByCompteSourceIdAndTypeTransactionAndDateTransactionAfter(Long compteId, String typeTransaction, Instant afterDate);

 @Query("SELECT t FROM Transaction t WHERE t.montant > 10000 and t.statut = 'PENDING'")
    List<Transaction> findImportantTransactions();

List<Transaction> findByMontantGreaterThanAndStatut(BigDecimal montant, String status);


    // If you want only incoming (destination)
    List<Transaction> findByCompteDestination_Id(Long compteId);
}
