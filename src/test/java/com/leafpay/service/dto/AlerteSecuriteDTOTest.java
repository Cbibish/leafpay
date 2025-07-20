package com.leafpay.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlerteSecuriteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteSecuriteDTO.class);
        AlerteSecuriteDTO alerteSecuriteDTO1 = new AlerteSecuriteDTO();
        alerteSecuriteDTO1.setId(1L);
        AlerteSecuriteDTO alerteSecuriteDTO2 = new AlerteSecuriteDTO();
        assertThat(alerteSecuriteDTO1).isNotEqualTo(alerteSecuriteDTO2);
        alerteSecuriteDTO2.setId(alerteSecuriteDTO1.getId());
        assertThat(alerteSecuriteDTO1).isEqualTo(alerteSecuriteDTO2);
        alerteSecuriteDTO2.setId(2L);
        assertThat(alerteSecuriteDTO1).isNotEqualTo(alerteSecuriteDTO2);
        alerteSecuriteDTO1.setId(null);
        assertThat(alerteSecuriteDTO1).isNotEqualTo(alerteSecuriteDTO2);
    }
}
