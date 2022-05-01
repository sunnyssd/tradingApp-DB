package com.example.demo.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.TradeService;

@EnableScheduling
@Component("TradeScheduler")
public class TradeScheduler {
	
	@Autowired
	private TradeService tradeService;
	
	@Scheduled(cron = "${update.expiry.for.matured.trades.cron}", zone = "${update.expiry.for.matured.trades.timezone}")
	public void triggerEvent() {
		System.out.println("triggerEvent Triggered");
		tradeService.updateExpiredTrade();
	}
}
