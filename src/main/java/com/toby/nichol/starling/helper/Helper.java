package com.toby.nichol.starling.helper;

import java.util.UUID;

public class Helper {

    public static String getAccountStatementUrl(String startDate, String endDate, String customerUid, String BASE_URL) {
        return BASE_URL + "/accounts/" + customerUid + "/statement/downloadForDateRange" + "?start=" + startDate + "&end=" + endDate;
    }

    public static String getAddToSavingsGoalUrl(String customerUid, String savingsGoalUid, UUID transferUid, String BASE_URL) {
        return BASE_URL + "/account/" + customerUid + "/savings-goals/" + savingsGoalUid + "/add-money/" + transferUid;
    }
}
