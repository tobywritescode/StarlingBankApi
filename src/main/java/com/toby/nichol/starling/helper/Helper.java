package com.toby.nichol.starling.helper;

public class Helper {
    

    private final static String BASE_URL = "https://api-sandbox.starlingbank.com/api/v2";

    public static String getAccountStatementUrl(String startDate, String endDate, String customerUid){
        return BASE_URL + "/accounts/" + customerUid + "/statement/downloadForDateRange" + "?start=" + startDate + "&end=" + endDate;
    }

}
