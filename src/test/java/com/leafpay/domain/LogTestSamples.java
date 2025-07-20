package com.leafpay.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Log getLogSample1() {
        return new Log().id(1L).action("action1").ipUtilisateur("ipUtilisateur1").resultat("resultat1");
    }

    public static Log getLogSample2() {
        return new Log().id(2L).action("action2").ipUtilisateur("ipUtilisateur2").resultat("resultat2");
    }

    public static Log getLogRandomSampleGenerator() {
        return new Log()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .ipUtilisateur(UUID.randomUUID().toString())
            .resultat(UUID.randomUUID().toString());
    }
}
