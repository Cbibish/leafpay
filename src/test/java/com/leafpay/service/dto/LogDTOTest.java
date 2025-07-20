package com.leafpay.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.leafpay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogDTO.class);
        LogDTO logDTO1 = new LogDTO();
        logDTO1.setId(1L);
        LogDTO logDTO2 = new LogDTO();
        assertThat(logDTO1).isNotEqualTo(logDTO2);
        logDTO2.setId(logDTO1.getId());
        assertThat(logDTO1).isEqualTo(logDTO2);
        logDTO2.setId(2L);
        assertThat(logDTO1).isNotEqualTo(logDTO2);
        logDTO1.setId(null);
        assertThat(logDTO1).isNotEqualTo(logDTO2);
    }
}
