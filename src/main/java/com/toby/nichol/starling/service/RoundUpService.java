package com.toby.nichol.starling.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RoundUpService {

    public Integer doRoundUp(List<BigDecimal> spendList){
        return spendList.stream().map(i -> {
            BigDecimal abs = i.abs();
            BigDecimal rounded = abs.setScale(0, RoundingMode.CEILING);
            return rounded.subtract(abs);
        }).reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(new BigDecimal("100")) // Multiply by 100
                .intValue();
    }
}
