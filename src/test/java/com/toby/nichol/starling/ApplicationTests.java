package com.toby.nichol.starling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("${bearer-token}")
	private String bearer;

	@Value("${customerUid}")
	private String customerUid;

	@Value("${savingsGoalUid}")
	private String savingsGoalUid;

	@Test
	void homeControllerShouldRunAndReturnOkGivenAValidRequest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", bearer);
		headers.set("Accept", "text/csv");
		this.mockMvc.perform(get("/roundup/run")
						.contentType(MediaType.APPLICATION_JSON)
						.param("customerid", customerUid)
						.param("savingsgoalid", savingsGoalUid)
						.param("startdate", "2024-12-09")
						.param("enddate", "2024-12-13")
						.headers(headers)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
