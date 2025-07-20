package com.leafpay.service.mapper;

import static com.leafpay.domain.AlerteSecuriteAsserts.*;
import static com.leafpay.domain.AlerteSecuriteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlerteSecuriteMapperTest {

    private AlerteSecuriteMapper alerteSecuriteMapper;

    @BeforeEach
    void setUp() {
        alerteSecuriteMapper = new AlerteSecuriteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlerteSecuriteSample1();
        var actual = alerteSecuriteMapper.toEntity(alerteSecuriteMapper.toDto(expected));
        assertAlerteSecuriteAllPropertiesEquals(expected, actual);
    }
}
