package com.toby.nichol.starling.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonSerialize
public class SavingsGoalRequest implements Serializable {
    private Amount amount;
}
