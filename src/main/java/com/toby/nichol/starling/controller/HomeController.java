package com.toby.nichol.starling.controller;

import com.toby.nichol.starling.model.SavingsGoalResponse;
import com.toby.nichol.starling.service.AccountService;
import com.toby.nichol.starling.service.RoundUpService;
import com.toby.nichol.starling.service.SavingsGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/roundup")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final RoundUpService roundUpService;

    @Autowired
    private final SavingsGoalService savingsGoalService;

    @GetMapping("/run")
    public ResponseEntity<SavingsGoalResponse> runApp(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                                                      @RequestParam("customerid") String customerUid, @RequestParam("savingsgoalid") String savingsGoalUid,
                                                      @RequestParam("startdate") String startDate, @RequestParam("enddate") String endDate ){
        List<BigDecimal> accountSpending = accountService.getAccountSpends(customerUid, startDate, endDate, bearer);
        Integer roundedUpSpending = roundUpService.doRoundUp(accountSpending);
        return savingsGoalService.addToSavingsGoal(roundedUpSpending, customerUid, savingsGoalUid, bearer);
    }

}
