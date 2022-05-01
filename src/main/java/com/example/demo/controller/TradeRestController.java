package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Trade;
import com.example.demo.service.TradeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
public class TradeRestController {

	@Autowired
	private TradeService tradeService;

	public TradeRestController() {
		super();
		// System.out.println("TradeController No Arg Constructor");
	}

	@GetMapping("/trades")
	public List<Trade> fetchAllTrades() {
		System.out.println("In fetchAllTrades method");
		return tradeService.retrieveAllTrades();
	}

	@GetMapping("/trade/{tradeId}")
	public ResponseEntity<String> fetchTradeById(@PathVariable("tradeId") String tradeId) {
		System.out.println("In fetchTradeById method");
		Optional<Trade> trade = tradeService.retrieveTradeUsingId(tradeId);
		if (trade.isPresent()) {
			String jsonTrade = "";
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	         try {
	             jsonTrade = mapper.writeValueAsString(tradeService.retrieveTradeUsingId(tradeId).get());
	             System.out.println("ResultingJSONstring = " + jsonTrade);
	         } catch (JsonProcessingException e) {
	             e.printStackTrace();
	         }
			return new ResponseEntity<String>(jsonTrade, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/trade")
	public ResponseEntity<String> saveTrade(@RequestBody Trade trade) {
		System.out.println("In saveTrade method");
		return tradeService.createTrade(trade);
	}

}
