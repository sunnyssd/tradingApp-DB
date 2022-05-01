package com.example.demo;

import static org.junit.Assert.assertEquals;
import static com.example.demo.util.TradeUtil.readFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//import com.example.demo.controller.TradeController;
import com.example.demo.model.Trade;
import com.example.demo.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradeStoreDbApplication.class, properties = "spring.config.location=src/main/resources/application.properties,src/test/resources/application.properties")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TradeAppTest {

	@Autowired
	private MockMvc mockmvc;
	/*
	@InjectMocks
	private TradeController tradeController;*/

	@Before
	public void load() {
		TradeService service = new TradeService();
	}

	@Test
	public void stage1_fetchAllTradesTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trades").accept(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();
		String actualResponse = responseMessage.getResponse().getContentAsString();
		actualResponse = replaceSpaces(actualResponse);
		
		String fileName = "src/test/resources/4_allRecords.json"; 
		String expectedResponse = readFile(fileName, StandardCharsets.UTF_8);
		expectedResponse = replaceSpaces(expectedResponse);

		assertEquals("Feched All Records Test Results : ", expectedResponse, actualResponse);
	}
	
	@Test
	public void stage2_fetchTradeUsingIdTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trade/T1").accept(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();
		String actualResponse = responseMessage.getResponse().getContentAsString();
		actualResponse = replaceSpaces(actualResponse);
		
		String fileName = "src/test/resources/5_recordT1.json"; 
		String expectedResponse = readFile(fileName, StandardCharsets.UTF_8);
		expectedResponse = replaceSpaces(expectedResponse);

		assertEquals("Feched Record Using Id Test Results : ", expectedResponse, actualResponse);
	}
	
	@Test
	public void stage3_testTradeAppForSuccess() throws Exception {

		Trade trade = new Trade("T5", 5.0, "CP-5", "B5", LocalDate.now().plusMonths(1), LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Valid Case Test Results : ", "success", actualResponse);
	}

	@Test
	public void stage4_testTradeAppForMaturityDateFailure() throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.US);
		LocalDate matrtydate = LocalDate.parse("2022-03-30", formatter);

		Trade trade = new Trade("T6", 6.0, "CP-6", "B6", matrtydate, LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		// String fileName = "src/test/resources/"; 
		// String expectedResponse = readFile(fileName, StandardCharsets.UTF_8);
		String expectedResponse = "Bad Request, Received Maturity Date [2022-03-30] , Should be >= Current Date [2022-05-02]";

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Before Maturity Date Test Results : ", expectedResponse, actualResponse);
	}

	@Test
	public void stage5_testTradeAppForLowerVersionFailure() throws Exception {
		Trade trade = new Trade("T2", 1.0, "CP-2", "B2", LocalDate.now().plusMonths(1), LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		String expectedResponse = "Bad Request, Received Version [1.0] is lesser than Existing version [2.0] For Trade Id : [T2]";

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Lesser Version Test Results : ", expectedResponse, actualResponse);
	}

	@Test
	public void stage6_testTradeAppForNullTradeIdFailure() throws Exception {
		Trade trade = new Trade(null, 1.0, "CP-2", "B2", LocalDate.now().plusMonths(1), LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		String expectedResponse = "Bad Request, Trade Id Cannot Be Empty or Null";

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Null Trade Id Test Results : ", expectedResponse, actualResponse);
	}

	@Test
	public void stage7_testTradeAppForNullMaturityDateFailure() throws Exception {
		Trade trade = new Trade("T2", 1.0, "CP-2", "B2", null, LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		String expectedResponse = "Bad Request, Maturity Date Cannot Be Empty or Null";

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Null Maturity Date Test Results : ", expectedResponse, actualResponse);
	}

	@Test
	public void stage8_testTradeAppForNullVersionFailure() throws Exception {
		Trade trade = new Trade("T2", null, "CP-2", "B2", LocalDate.now().plusMonths(1), LocalDate.now(), "N");
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String jsonString = mapper.writeValueAsString(trade);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trade").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult responseMessage = mockmvc.perform(requestBuilder).andReturn();

		String expectedResponse = "Bad Request, Trade Version Cannot Be Empty or Null";

		String actualResponse = responseMessage.getResponse().getContentAsString();

		assertEquals("Null Version Test Results : ", expectedResponse, actualResponse);
	}
	
	private String replaceSpaces(String response) {
		response = response.replaceAll("\\s+", "");
		return response;
	}
}
