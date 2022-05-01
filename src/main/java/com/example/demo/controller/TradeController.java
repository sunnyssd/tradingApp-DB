package com.example.demo.controller;

import static com.example.demo.util.TradeUtil.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.model.Trade;
import com.example.demo.service.LoginService;
import com.example.demo.service.TradeService;

@Controller
@SessionAttributes("userName")
public class TradeController {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private LoginService loginService;

	public TradeController() {
		super();
		// System.out.println("TradeController No Arg Constructor");
	}

	@RequestMapping(value = "/")
	public String home() {
		System.out.println("In home Method");
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		System.out.println("In login Method");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String validateUser(ModelMap model, @RequestParam("userName") String userName,
			@RequestParam("password") String password) {

		System.out.println("In validateUser Method");
		if (loginService.isValidUser(userName, password)) {
			System.out.println("Valid User");
			model.put("userName", userName);
			model.put("password", password);
			return "welcome";
		} else {
			System.out.println("InValid User");
			model.put("errorMsg", "Invalid Credentials, check in property file for credentials");
			return "login";
		}
	}

	@RequestMapping(value = "/list-trades", method = RequestMethod.GET)
	public String retrieveTrades(ModelMap model) {
		System.out.println("In retrieveTrades Method");
		List<Trade> tradeList = tradeService.retrieveAllTrades();
		model.put("tradeList", tradeList);
		return "showTrades";
	}

	@RequestMapping(value = "/create-trade", method = RequestMethod.GET)
	public String createNewTradeMethod() {
		System.out.println("In createNewTradeMethod Method");
		return "createTrade";
	}

	@RequestMapping(value = "/create-trade", method = RequestMethod.POST)
	public String getDetailsOfNewTrade(ModelMap model, @RequestParam String tradeId,
			// @RequestParam(value = "version", required=false) Integer version,
			@RequestParam String strVersion, @RequestParam String counterPartyId, @RequestParam String bookId,
			@RequestParam String maturityDate, @RequestParam String expired) {
		System.out.println("In getDetailsOfNewTrade Method");

		if (isEmpty(tradeId) || isEmpty(strVersion) || isEmpty(maturityDate)) {
			String error = "Trade Id [" + tradeId + "] AND Maturity Date [" + maturityDate + "] AND Version ["
					+ strVersion + "] Cannot Be Empty or Null";
			System.out.println(error);

			model.put("responseBody", new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase() + ", " + error,
					HttpStatus.BAD_REQUEST).getBody());
			return "errorTrade";
		}

		Double version = 0.0;
		try {
			version = Double.valueOf(strVersion);
		} catch (NumberFormatException nfe) {
			String error = "Version [" + strVersion + "] is Not Numeric, Please Enter Valid Version";
			System.out.println(error);

			model.put("responseBody", new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase() + ", " + error,
					HttpStatus.BAD_REQUEST).getBody());
			return "errorTrade";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.US);
		LocalDate matrtydate = LocalDate.parse(maturityDate, formatter);
		ResponseEntity<String> responseEntity = tradeService
				.createTrade(new Trade(tradeId, version, counterPartyId, bookId, matrtydate, LocalDate.now(), expired));
		model.put("responseBody", responseEntity.getBody());
		if (!(responseEntity.getStatusCodeValue() == 200)) {
			return "errorTrade";
		} else {
			return "redirect:/list-trades";
		}
	}
}
