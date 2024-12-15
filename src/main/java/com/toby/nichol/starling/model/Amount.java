package com.toby.nichol.starling.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Amount {
    private String currency;
    private Integer minorUnits;
}
