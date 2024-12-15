package com.toby.nichol.starling.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RoundUpServiceTest {

    @Autowired
    RoundUpService roundUpService;

    @Test
    public void RoundUpServiceShouldCorrectlyRoundUpValuesGivenValidRequest() {
        List<BigDecimal> inputBigDecimalList = List.of(
                new BigDecimal("-34.30"),
                new BigDecimal("-7.60"),
                new BigDecimal("-28.03"),
                new BigDecimal("-25.63"),
                new BigDecimal("-6.41"),
                new BigDecimal("-32.72"));
//       0.70 + 0.40 + 0.97 + 0.37 + 0.59 + 0.28 = 3.31*100=331
        Integer expected = 331;
        Integer actual = roundUpService.doRoundUp(inputBigDecimalList);
        assertEquals(expected, actual);
    }

}