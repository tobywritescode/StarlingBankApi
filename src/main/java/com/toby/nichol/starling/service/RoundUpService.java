package com.toby.nichol.starling.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RoundUpService {

    //Simple stream to calculate the total round up value. We take in a list of BigDecimal values, absolute them so they are positive numbers, round up each value.
    // then subtract the original statement value from the rounded up value to get our round up remainder. This is then multiplied by 100 and returned as an int to give us the roundup total as a minor unit.
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
