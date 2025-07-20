package com.leafpay.service.mapper;

import static com.leafpay.domain.UtilisateurAsserts.*;
import static com.leafpay.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilisateurMapperTest {

    private UtilisateurMapper utilisateurMapper;

    @BeforeEach
    void setUp() {
        utilisateurMapper = new UtilisateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtilisateurSample1();
        var actual = utilisateurMapper.toEntity(utilisateurMapper.toDto(expected));
        assertUtilisateurAllPropertiesEquals(expected, actual);
    }
}
