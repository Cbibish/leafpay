package com.leafpay.domain;

import static com.leafpay.domain.AlerteSecuriteTestSamples.*;
import static com.leafpay.domain.LogTestSamples.*;
import static com.leafpay.domain.RoleTestSamples.*;
import static com.leafpay.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void idRoleTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        utilisateur.setIdRole(roleBack);
        assertThat(utilisateur.getIdRole()).isEqualTo(roleBack);

        utilisateur.idRole(null);
        assertThat(utilisateur.getIdRole()).isNull();
    }

    @Test
    void logTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Log logBack = getLogRandomSampleGenerator();

        utilisateur.addLog(logBack);
        assertThat(utilisateur.getLogs()).containsOnly(logBack);
        assertThat(logBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeLog(logBack);
        assertThat(utilisateur.getLogs()).doesNotContain(logBack);
        assertThat(logBack.getUtilisateur()).isNull();

        utilisateur.logs(new HashSet<>(Set.of(logBack)));
        assertThat(utilisateur.getLogs()).containsOnly(logBack);
        assertThat(logBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setLogs(new HashSet<>());
        assertThat(utilisateur.getLogs()).doesNotContain(logBack);
        assertThat(logBack.getUtilisateur()).isNull();
    }

    @Test
    void alerteSecuriteTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        AlerteSecurite alerteSecuriteBack = getAlerteSecuriteRandomSampleGenerator();

        utilisateur.addAlerteSecurite(alerteSecuriteBack);
        assertThat(utilisateur.getAlerteSecurites()).containsOnly(alerteSecuriteBack);
        assertThat(alerteSecuriteBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeAlerteSecurite(alerteSecuriteBack);
        assertThat(utilisateur.getAlerteSecurites()).doesNotContain(alerteSecuriteBack);
        assertThat(alerteSecuriteBack.getUtilisateur()).isNull();

        utilisateur.alerteSecurites(new HashSet<>(Set.of(alerteSecuriteBack)));
        assertThat(utilisateur.getAlerteSecurites()).containsOnly(alerteSecuriteBack);
        assertThat(alerteSecuriteBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setAlerteSecurites(new HashSet<>());
        assertThat(utilisateur.getAlerteSecurites()).doesNotContain(alerteSecuriteBack);
        assertThat(alerteSecuriteBack.getUtilisateur()).isNull();
    }
}
