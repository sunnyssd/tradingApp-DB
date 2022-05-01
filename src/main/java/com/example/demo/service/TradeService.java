package com.example.demo.service;

import static com.example.demo.util.TradeUtil.isEmpty;
import static com.example.demo.util.TradeUtil.isNotEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TradeRepo;
import com.example.demo.exception.TradeException;
import com.example.demo.exception.TradeTransformException;
import com.example.demo.model.Trade;

@Service
public class TradeService {

	@Autowired
	private TradeRepo tradeRepo;

	public TradeService() {
		// System.out.println("TradeService class no arg constructor");
	}

	public List<Trade> retrieveAllTrades() {
		return tradeRepo.findAll();
	}

	public Optional<Trade> retrieveTradeUsingId(String tradeId) {
		return tradeRepo.findById(tradeId);
	}

	public void saveTrade(Trade trade) throws TradeException {
		System.out.println("saveTrade method");
		try {
			tradeRepo.save(trade);
		} catch (Exception e) {
			throw new TradeException(e.getMessage(), e);
		}
		System.out.println("Trade Saved");
	}

	public ResponseEntity<String> createTrade(Trade trade) {
		System.out.println("createTrade method");
		try {
			if (checkIfTradeShouldBeSaved(trade)) {
				saveTrade(trade);
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				String error = "Received Maturity Date [" + trade.getMaturityDate() + "] , Should be >= Current Date ["
						+ LocalDate.now() + "]";
				System.out.println(error);
				throw new TradeTransformException(error);
			}
		} catch (TradeTransformException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase() + ", " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		} catch (TradeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ", " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}

	}

	public void updateExpiredTrade() {
		System.out.println("updateExpiredTrade method");
		tradeRepo.findAll().stream().filter(trade -> trade.getMaturityDate().isBefore(LocalDate.now()))
				.forEach(trade -> {
					try {
						updateTrades(trade);
					} catch (TradeException e) {
						e.printStackTrace();
					}
				});
		System.out.println("Trades Updated");
	}

	public void updateTrades(Trade trade) throws TradeException {
		System.out.println("updateTrades method");
		trade.setExpired("Y");
		saveTrade(trade);
	}

	public boolean checkIfTradeShouldBeSaved(Trade trade) throws TradeTransformException {
		boolean flag = false;
		if (!checkIfMaturityDateLessThanCurrentDate(trade.getMaturityDate())) {
			System.out.println("Received Trade is fine in terms of Maturity Date");
			if (checkIfReceivedVersionShouldBeSaved(trade)) {
				System.out.println("Received Trade is fine in terms of Version");
				flag = true;
			}
		}
		return flag;
	}

	private boolean checkIfReceivedVersionShouldBeSaved(Trade receivedTrade) throws TradeTransformException {
		if (isEmpty(receivedTrade.getVersion())) {
			String error = "Trade Version Cannot Be Empty or Null";
			System.out.println(error);
			throw new TradeTransformException(error);
		}
		if (isNotEmpty(receivedTrade.getTradeId())) {
			Optional<Trade> existingTrade = retrieveTradeUsingId(receivedTrade.getTradeId());
			if (existingTrade.isPresent()) {
				if (receivedTrade.getVersion() >= existingTrade.get().getVersion()) {
					System.out.println("Received Version is Greater than or Equal to Existing version");
					return true;
				} else {
					String error = "Received Version [" + receivedTrade.getVersion()
							+ "] is lesser than Existing version [" + existingTrade.get().getVersion() + "]"
							+ " For Trade Id : [" + existingTrade.get().getTradeId() + "]";

					System.out.println(error);
					throw new TradeTransformException(error);
				}
			} else {
				return true;
			}
		} else {
			String error = "Trade Id Cannot Be Empty or Null";
			System.out.println(error);
			throw new TradeTransformException(error);
		}

	}

	private boolean checkIfMaturityDateLessThanCurrentDate(LocalDate receivedMaturityDate)
			throws TradeTransformException {
		if (isNotEmpty(receivedMaturityDate)) {
			return receivedMaturityDate.isBefore(LocalDate.now()) ? true : false;
		} else {
			String error = "Maturity Date Cannot Be Empty or Null";
			System.out.println(error);
			throw new TradeTransformException(error);
		}
	}

}
