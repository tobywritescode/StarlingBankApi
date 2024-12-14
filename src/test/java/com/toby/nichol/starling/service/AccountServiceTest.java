package com.toby.nichol.starling.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.List;

import static com.toby.nichol.starling.helper.TestHelper.getAccountStatementUrl;
import static com.toby.nichol.starling.helper.TestHelper.getStub;
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
        getStub("/data/statement.csv", getAccountStatementUrl(BASE_URL, "2024-12-09", "2024-12-13", customerUid), HttpURLConnection.HTTP_OK, bearer);
        List<BigDecimal> expectedStatementOutput = List.of(
                new BigDecimal("-34.30"),
                new BigDecimal("-7.60"),
                new BigDecimal("-28.03"),
                new BigDecimal("-25.63"),
                new BigDecimal("-6.41"),
                new BigDecimal("-32.72"));
        List<BigDecimal> actual = accountService.getAccountSpends(customerUid, bearer);
        assertEquals(expectedStatementOutput.size(), actual.size());
        assertEquals(expectedStatementOutput, actual);
    }

}