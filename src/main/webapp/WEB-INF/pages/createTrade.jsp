<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<h1>Create New Trade Here</h1>
		<form method="post">
			Trade Id			:	<input type="text" name="tradeId" required/></br>
			Version				:	<input type="text" name="strVersion" required/></br>
			Counter Party Id	:	<input type="text" name="counterPartyId"/></br>
			Book Id				:	<input type="text" name="bookId"/></br>
			Maturity Date		:	<input type="date" name="maturityDate" required/></br>
			Expired				:	<input type="text" name="expired"/></br>
									<input type="submit" name="createTradeObj"/></br>		
		</form>

</body>
</html>