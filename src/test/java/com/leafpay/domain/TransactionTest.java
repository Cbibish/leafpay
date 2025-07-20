package com.leafpay.domain;

import static com.leafpay.domain.CompteTestSamples.*;
import static com.leafpay.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void compteSourceTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        transaction.setCompteSource(compteBack);
        assertThat(transaction.getCompteSource()).isEqualTo(compteBack);

        transaction.compteSource(null);
        assertThat(transaction.getCompteSource()).isNull();
    }

    @Test
    void compteDestinationTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        transaction.setCompteDestination(compteBack);
        assertThat(transaction.getCompteDestination()).isEqualTo(compteBack);

        transaction.compteDestination(null);
        assertThat(transaction.getCompteDestination()).isNull();
    }
}
