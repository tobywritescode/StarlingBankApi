package com.toby.nichol.starling.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {
    private String message;
}
