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
    //application requires 4 params to run; auth bearer, customer UUID, savings goal UUID, start date and end date.
    public ResponseEntity<SavingsGoalResponse> runApp(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                                                      @RequestParam("customerid") String customerUid, @RequestParam("savingsgoalid") String savingsGoalUid,
                                                      @RequestParam("startdate") String startDate, @RequestParam("enddate") String endDate ){
        //Return a list of bigdecimals (spending) from the account service
        List<BigDecimal> accountSpending = accountService.getAccountSpends(customerUid, startDate, endDate, bearer);
        //Use the roundup service to calculate the rounde dup value in minor units to add to a savings goal
        Integer roundedUpSpending = roundUpService.doRoundUp(accountSpending);
        //Add the value to the savings goal provided and return the response from the Starling api.
        return savingsGoalService.addToSavingsGoal(roundedUpSpending, customerUid, savingsGoalUid, bearer);
    }

}
