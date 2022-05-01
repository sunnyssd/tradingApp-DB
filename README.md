# tradingApp-DB
Trading Application For DB
created 2 Controllers, One for Human Readibility where one will be gievn JSP pages rendered
The Other is RestController, which would deal in JSON

Controller Can Be Tested Using Browser : http://localhost:8080/, username/password in property file
RestController Can Be Tested Using Postman: 
      GET : http://localhost:8080/trades to fetch All Trades
      GET : http://localhost:8080/trade/T1 To fetch Specific Trade
      POST : http://localhost:8080/trade to create new trade, sample available in test resources
Have used in memory database H2, can be accessed at http://localhost:8080/h2-console/
      JDBC url given in property file (jdbc:h2:mem:trade), 4 records are precreated
