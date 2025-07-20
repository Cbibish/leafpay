package com.leafpay.domain;

import static com.leafpay.domain.CompteTestSamples.*;
import static com.leafpay.domain.UtilisateurCompteTestSamples.*;
import static com.leafpay.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisateurCompteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisateurCompte.class);
        UtilisateurCompte utilisateurCompte1 = getUtilisateurCompteSample1();
        UtilisateurCompte utilisateurCompte2 = new UtilisateurCompte();
        assertThat(utilisateurCompte1).isNotEqualTo(utilisateurCompte2);

        utilisateurCompte2.setId(utilisateurCompte1.getId());
        assertThat(utilisateurCompte1).isEqualTo(utilisateurCompte2);

        utilisateurCompte2 = getUtilisateurCompteSample2();
        assertThat(utilisateurCompte1).isNotEqualTo(utilisateurCompte2);
    }

    @Test
    void utilisateurTest() {
        UtilisateurCompte utilisateurCompte = getUtilisateurCompteRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        utilisateurCompte.setUtilisateur(utilisateurBack);
        assertThat(utilisateurCompte.getUtilisateur()).isEqualTo(utilisateurBack);

        utilisateurCompte.utilisateur(null);
        assertThat(utilisateurCompte.getUtilisateur()).isNull();
    }

    @Test
    void compteTest() {
        UtilisateurCompte utilisateurCompte = getUtilisateurCompteRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        utilisateurCompte.setCompte(compteBack);
        assertThat(utilisateurCompte.getCompte()).isEqualTo(compteBack);

        utilisateurCompte.compte(null);
        assertThat(utilisateurCompte.getCompte()).isNull();
    }
}
