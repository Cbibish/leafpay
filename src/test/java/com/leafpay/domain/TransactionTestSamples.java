package com.leafpay.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).typeTransaction("typeTransaction1").statut("statut1").moyenValidation("moyenValidation1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).typeTransaction("typeTransaction2").statut("statut2").moyenValidation("moyenValidation2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .typeTransaction(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString())
            .moyenValidation(UUID.randomUUID().toString());
    }
}
