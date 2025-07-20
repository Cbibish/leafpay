package com.leafpay.service.mapper;

import static com.leafpay.domain.UtilisateurCompteAsserts.*;
import static com.leafpay.domain.UtilisateurCompteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilisateurCompteMapperTest {

    private UtilisateurCompteMapper utilisateurCompteMapper;

    @BeforeEach
    void setUp() {
        utilisateurCompteMapper = new UtilisateurCompteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtilisateurCompteSample1();
        var actual = utilisateurCompteMapper.toEntity(utilisateurCompteMapper.toDto(expected));
        assertUtilisateurCompteAllPropertiesEquals(expected, actual);
    }
}
