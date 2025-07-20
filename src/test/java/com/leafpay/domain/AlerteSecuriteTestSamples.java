package com.leafpay.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteSecuriteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlerteSecurite getAlerteSecuriteSample1() {
        return new AlerteSecurite().id(1L).typeAlerte("typeAlerte1").niveauSeverite("niveauSeverite1");
    }

    public static AlerteSecurite getAlerteSecuriteSample2() {
        return new AlerteSecurite().id(2L).typeAlerte("typeAlerte2").niveauSeverite("niveauSeverite2");
    }

    public static AlerteSecurite getAlerteSecuriteRandomSampleGenerator() {
        return new AlerteSecurite()
            .id(longCount.incrementAndGet())
            .typeAlerte(UUID.randomUUID().toString())
            .niveauSeverite(UUID.randomUUID().toString());
    }
}
