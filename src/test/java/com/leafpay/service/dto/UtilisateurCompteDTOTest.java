package com.leafpay.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisateurCompteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisateurCompteDTO.class);
        UtilisateurCompteDTO utilisateurCompteDTO1 = new UtilisateurCompteDTO();
        utilisateurCompteDTO1.setId(1L);
        UtilisateurCompteDTO utilisateurCompteDTO2 = new UtilisateurCompteDTO();
        assertThat(utilisateurCompteDTO1).isNotEqualTo(utilisateurCompteDTO2);
        utilisateurCompteDTO2.setId(utilisateurCompteDTO1.getId());
        assertThat(utilisateurCompteDTO1).isEqualTo(utilisateurCompteDTO2);
        utilisateurCompteDTO2.setId(2L);
        assertThat(utilisateurCompteDTO1).isNotEqualTo(utilisateurCompteDTO2);
        utilisateurCompteDTO1.setId(null);
        assertThat(utilisateurCompteDTO1).isNotEqualTo(utilisateurCompteDTO2);
    }
}
