package com.toby.nichol.starling.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WireMockTest(httpPort = 8081)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AccountServiceTest {


    @Value("${base_url}")
    private String BASE_URL;

    @Value("${customerUid}")
    private String customerUid;

    @Value("${bearer-token}")
    private String bearer;

    @Autowired
    AccountService accountService;

    @Test
    public void AccountServiceShouldGetAccountStatementForGivenWeek() throws IOException {
        getStub(bearer);
        String startDate = "2024-12-09";
        String endDate = "2024-12-13";
        List<BigDecimal> expectedStatementOutput = List.of(new BigDecimal("-34.30"), new BigDecimal("-7.60"), new BigDecimal("-28.03"), new BigDecimal("-25.63"), new BigDecimal("-6.41"), new BigDecimal("-32.72"));
        List<BigDecimal> actual = accountService.getAccountSpends(customerUid, startDate, endDate, bearer);
        //assert that we have the right amount of values and they are the values we expect.
        assertEquals(expectedStatementOutput.size(), actual.size());
        assertEquals(expectedStatementOutput, actual);
    }

    //generates a wiremock stub to be tested against. Will return a statement response in csv format for any request matching the pattern below.
    private void getStub(String bearer) throws IOException {
        String path = "src/test/resources"+ "/data/statement.csv";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String JsonString = IOUtils.toString(
                Objects.requireNonNull(
                        Files.newInputStream(
                                Paths.get(
                                        absolutePath))), "UTF-8");
        stubFor(any(urlPathMatching("/api/v2/accounts/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}/statement/downloadForDateRange"))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withHeader("Accept", "text/csv")
                        .withHeader("Authorization", bearer)
                        .withBody(JsonString)));
    }

}