package com.leafpay.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurCompteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UtilisateurCompte getUtilisateurCompteSample1() {
        return new UtilisateurCompte().id(1L).roleUtilisateurSurCeCompte("roleUtilisateurSurCeCompte1");
    }

    public static UtilisateurCompte getUtilisateurCompteSample2() {
        return new UtilisateurCompte().id(2L).roleUtilisateurSurCeCompte("roleUtilisateurSurCeCompte2");
    }

    public static UtilisateurCompte getUtilisateurCompteRandomSampleGenerator() {
        return new UtilisateurCompte().id(longCount.incrementAndGet()).roleUtilisateurSurCeCompte(UUID.randomUUID().toString());
    }
}
