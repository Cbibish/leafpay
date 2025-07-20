package com.leafpay.service.mapper;

import static com.leafpay.domain.CompteAsserts.*;
import static com.leafpay.domain.CompteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompteMapperTest {

    private CompteMapper compteMapper;

    @BeforeEach
    void setUp() {
        compteMapper = new CompteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompteSample1();
        var actual = compteMapper.toEntity(compteMapper.toDto(expected));
        assertCompteAllPropertiesEquals(expected, actual);
    }
}
