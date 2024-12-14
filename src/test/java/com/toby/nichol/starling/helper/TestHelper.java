package com.toby.nichol.starling.helper;

import wiremock.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class TestHelper {

    public static void getStub(String resourceFileName, String url, int code, String bearer) throws IOException {
        String path = "src/test/resources"+resourceFileName;
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String JsonString = IOUtils.toString(
                Objects.requireNonNull(
                        Files.newInputStream(
                                Paths.get(
                                        absolutePath))), "UTF-8");
        stubFor(any(urlPathEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(code)
                        .withHeader("Content-Type", "text/html")
                        .withHeader("Authorization", bearer)
                        .withBody(JsonString)));
    }

    public static String getAccountStatementUrl(String BASE_URL, String startDate, String endDate, String customerUid){
        return BASE_URL + "/accounts/" + customerUid + "/statement/downloadForDateRange" + "?start=" + startDate + "&end=" + endDate;
    }
}
