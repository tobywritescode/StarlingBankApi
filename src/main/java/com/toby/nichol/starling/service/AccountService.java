package com.toby.nichol.starling.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.toby.nichol.starling.helper.Helper.getAccountStatementUrl;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RestTemplate restTemplate;

    @Value("${base_url}")
    private String BASE_URL;

    public List<BigDecimal> getAccountSpends(String customerUid, String startDate, String endDate, String bearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearer);
        headers.set("Accept", "text/csv");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url =  getAccountStatementUrl(startDate, endDate, customerUid, BASE_URL);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        String responseBody;
        if(response.getStatusCode().value() == 200){
            responseBody = response.getBody();
        }else{
            throw new RuntimeException("Could not retrieve statement.");
        }
        return getSpendingFromStatement(responseBody);
    }

    private List<BigDecimal> getSpendingFromStatement(String responseBody) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();
        List<BigDecimal> amounts;
        try(MappingIterator<Map<String, String>> iterator = mapper.readerFor(Map.class)
                .with(headerSchema)
                .readValues(responseBody)){
            List<Map<String, String>> csvColumns = iterator.readAll();

            amounts = csvColumns.stream()
                    .map(map -> map.get("Amount (GBP)"))
                    .map(BigDecimal::new)
                    .filter(number -> number.compareTo(BigDecimal.ZERO) < 0)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return amounts;
    }


}
