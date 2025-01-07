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

    //Method gets CSV file of the customer statement for the given week and returns the method call getSpendingFromStatement(responseBody)
    //getSpendingFromStatement returns a list of BigDecimal values less than 0 from the CSV data
    public List<BigDecimal> getAccountSpends(String customerUid, String startDate, String endDate, String bearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearer);
        headers.set("Accept", "text/csv"); //<- accept the CSV file format
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url =  getAccountStatementUrl(startDate, endDate, customerUid, BASE_URL); //<- build the header and request url.
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        String responseBody;
        if(response.getStatusCode().value() == 200){ //<- if 200 response we can get the statement as a string.
            responseBody = response.getBody();
        }else{
            throw new RuntimeException("Could not retrieve statement.");
        }
        return getSpendingFromStatement(responseBody);
    }

    //As we don't need the entire transaction the following code parses the CSV object
    // then, using a stream, isolates the amount column, formats the values to BigDecimal and returns only values less than 0.
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
        if(amounts.isEmpty()){
            
        }
    }


}
