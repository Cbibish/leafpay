package com.leafpay.service.mapper;

import static com.leafpay.domain.LogAsserts.*;
import static com.leafpay.domain.LogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogMapperTest {

    private LogMapper logMapper;

    @BeforeEach
    void setUp() {
        logMapper = new LogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLogSample1();
        var actual = logMapper.toEntity(logMapper.toDto(expected));
        assertLogAllPropertiesEquals(expected, actual);
    }
}
