package com.toby.nichol.starling.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.toby.nichol.starling.model.Error;
import com.toby.nichol.starling.model.SavingsGoalResponse;
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
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 8081)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SavingsGoalServiceTest {

    @Value("${base_url}")
    private String BASE_URL;

    @Value("${customerUid}")
    private String customerUid;

    @Value("${savingsGoalUid}")
    private String savingsGoalUid;

    @Value("${bearer-token}")
    private String bearer;

    @Autowired
    SavingsGoalService savingsGoalService;


    @Test
    void addToSavingsGoalShouldReturnSavingsGoalResponseGivenValidRequest() throws IOException {
        UUID transferUuid = UUID.fromString("bddbaaba-51fd-4e70-8176-d23e84705bd2");
        getStub(transferUuid);
        SavingsGoalResponse expected = SavingsGoalResponse.builder().success(Boolean.TRUE).transferUid(transferUuid).build();
        SavingsGoalResponse actual = savingsGoalService.addToSavingsGoal(1, customerUid, savingsGoalUid, bearer).getBody();
        assertEquals(expected, actual);
    }

    @Test
    void addToSavingsGoalShouldReturnSavingsGoalErrorResponseGivenInvalidRequest() throws IOException {
        UUID transferUuid = UUID.fromString("bddbaaba-51fd-4e70-8176-d23e84705bd2");
        getErrorStub(transferUuid);
        SavingsGoalResponse expected = SavingsGoalResponse.builder().success(Boolean.FALSE).transferUid(null).errors(List.of(Error.builder().message("AMOUNT_MUST_BE_POSITIVE").build())).build();
        SavingsGoalResponse actual = savingsGoalService.addToSavingsGoal(1, customerUid, savingsGoalUid, bearer).getBody();
        assertEquals(expected, actual);
    }

    //generates a wiremock stub to be tested against. Will return a savingsGoal response for any request matching the pattern below.
    private void getStub(UUID transferUuid) throws IOException {
        String path = "src/test/resources"+ "/data/savingsGoalResponse.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String JsonString = IOUtils.toString(
                Objects.requireNonNull(
                        Files.newInputStream(
                                Paths.get(
                                        absolutePath))), "UTF-8");
        stubFor(any(urlPathMatching("/api/v2/account/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}/savings-goals/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}/add-money/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}"))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(JsonString)));
    }

    //generates a wiremock stub to be tested against. Will return a savingsGoal response for any request matching the pattern below.
    private void getErrorStub(UUID transferUuid) throws IOException {
        String path = "src/test/resources"+ "/data/error.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String JsonString = IOUtils.toString(
                Objects.requireNonNull(
                        Files.newInputStream(
                                Paths.get(
                                        absolutePath))), "UTF-8");
        stubFor(any(urlPathMatching("/api/v2/account/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}/savings-goals/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}/add-money/[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}"))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_BAD_REQUEST)
                        .withHeader("Content-Type", "application/json")
                        .withBody(JsonString)));
    }
}