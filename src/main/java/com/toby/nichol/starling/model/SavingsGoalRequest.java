package com.toby.nichol.starling.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonSerialize
public class SavingsGoalRequest {
    private Amount amount;
}
