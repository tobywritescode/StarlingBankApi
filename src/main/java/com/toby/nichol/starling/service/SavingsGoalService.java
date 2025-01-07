package com.toby.nichol.starling.service;

import com.toby.nichol.starling.model.Amount;
import com.toby.nichol.starling.model.SavingsGoalRequest;
import com.toby.nichol.starling.model.SavingsGoalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.toby.nichol.starling.helper.Helper.getAddToSavingsGoalUrl;

@Service
@RequiredArgsConstructor
public class SavingsGoalService {

    private final RestTemplate restTemplate;

    @Value("${base_url}")
    private String BASE_URL;

    public ResponseEntity<SavingsGoalResponse> addToSavingsGoal(Integer roundedUpSpending, String customerUid, String savingsGoalUid, String bearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer);
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        SavingsGoalRequest savingsGoalRequest = SavingsGoalRequest.builder().amount(Amount.builder().currency("GBP").minorUnits(roundedUpSpending).build()).build();
        HttpEntity<SavingsGoalRequest> entity = new HttpEntity<>(savingsGoalRequest, headers);
        String url =  getAddToSavingsGoalUrl(customerUid, savingsGoalUid, UUID.randomUUID(), BASE_URL);

        try{
            return restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    SavingsGoalResponse.class
            );
        }catch (HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(SavingsGoalResponse.class));
        }


    }
}
