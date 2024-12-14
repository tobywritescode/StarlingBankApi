package com.toby.nichol.starling.controller;

import com.toby.nichol.starling.model.SavingsGoalResponse;
import com.toby.nichol.starling.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roundup")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private final AccountService accountService;

    @GetMapping("/run")
    public ResponseEntity<SavingsGoalResponse> runApp(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer, @RequestParam("customerid") String customerUid, @RequestParam("savingsgoalid") String savingsGoalUid ) throws IOException {
        List<BigDecimal> accountSpending = accountService.getAccountSpends(customerUid, bearer);
        SavingsGoalResponse savingsGoalResponse = SavingsGoalResponse.builder().transferUid(UUID.randomUUID()).success(Boolean.TRUE).build();
        return ResponseEntity.ok(savingsGoalResponse);
    }

}
