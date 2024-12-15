# Toby Nichol - Starling Tech Test - RoundUpService

**Description:**

This project is my submission of the Starling tech test.

**Getting Started:**

1.  **Prerequisites:**
    *   Java 17
    

2.  **Installation:**
    *   Clone the repository: `git clone https://github.com/tobywritescode/starling_tech_test.git`

**Test Usage:**
* To run the tests, port 8081 needs to be available for wiremock. This requires the application-test.properties file to be updated with the relevant params (customerUUID, savingsGoalUUID and the bearer token)
* The e2e test requires the same parameters to be updated in the application-e2e.properties file as the wiremock and e2e tests run on different profiles.

**Usage:**

*   To start the application you just need to have port 8080 available.
* The endpoint is /roundup/run which requires a bearer token as the authorization header and it takes 4 parameters: the customer UUID, the savings goal UUID, the start date and end date (formatted as yyyy-mm-dd) of the week you wish to run the round up service against.
* e.g `localhost:8080/roundup/run?customerid=fc1e99ec-c647-4955-a7d3-637cb27869f7&savingsgoalid=fc48210f-0402-4861-a05c-70fdc4f01542&startdate=2024-12-09&enddate=2024-12-13`

**Contact:**

[Toby Nichol] - [toby.s.nichol@gmail.com]