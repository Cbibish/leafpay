package com.leafpay.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Compte getCompteSample1() {
        return new Compte().id(1L).typeCompte("typeCompte1").limiteRetraitsMensuels(1).statut("statut1").iban("iban1");
    }

    public static Compte getCompteSample2() {
        return new Compte().id(2L).typeCompte("typeCompte2").limiteRetraitsMensuels(2).statut("statut2").iban("iban2");
    }

    public static Compte getCompteRandomSampleGenerator() {
        return new Compte()
            .id(longCount.incrementAndGet())
            .typeCompte(UUID.randomUUID().toString())
            .limiteRetraitsMensuels(intCount.incrementAndGet())
            .statut(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString());
    }
}
