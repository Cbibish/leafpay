package com.leafpay.domain;

import static com.leafpay.domain.AlerteSecuriteTestSamples.*;
import static com.leafpay.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlerteSecuriteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteSecurite.class);
        AlerteSecurite alerteSecurite1 = getAlerteSecuriteSample1();
        AlerteSecurite alerteSecurite2 = new AlerteSecurite();
        assertThat(alerteSecurite1).isNotEqualTo(alerteSecurite2);

        alerteSecurite2.setId(alerteSecurite1.getId());
        assertThat(alerteSecurite1).isEqualTo(alerteSecurite2);

        alerteSecurite2 = getAlerteSecuriteSample2();
        assertThat(alerteSecurite1).isNotEqualTo(alerteSecurite2);
    }

    @Test
    void utilisateurTest() {
        AlerteSecurite alerteSecurite = getAlerteSecuriteRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        alerteSecurite.setUtilisateur(utilisateurBack);
        assertThat(alerteSecurite.getUtilisateur()).isEqualTo(utilisateurBack);

        alerteSecurite.utilisateur(null);
        assertThat(alerteSecurite.getUtilisateur()).isNull();
    }
}
