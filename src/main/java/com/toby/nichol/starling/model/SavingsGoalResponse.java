package com.toby.nichol.starling.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingsGoalResponse {
    private UUID transferUid;
    private List<Error> errors;
    private Boolean success;
}
