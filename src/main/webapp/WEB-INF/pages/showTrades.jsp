<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Trades for ${name}</title>
<link href="webjars/bootstrap/4.5.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

	<div class="container">
		<table class="table table-striped">
			<caption>Your Trades Are</caption>
			<thead>
				<tr>
					<th>Trade Id</th>
					<th>Version</th>
					<th>Counter-Party Id</th>
					<th>Book-Id</th>
					<th>Maturity Date</th>
					<th>Created Date</th>
					<th>Expired</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tradeList}" var="trade">
					<tr>
						<td>${trade.tradeId}</td>
						<td>${trade.version}</td>
						<td>${trade.counterPartyId}</td>
						<td>${trade.bookId}</td>
						<td>${trade.maturityDate}</td> 
						<td>${trade.createdDate}</td> 
						<td>${trade.expired}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div>
			<a class="button" href="/create-trade">Click Here</a> To Create New Trade </br>
			<a class="button" href="/">Click Here</a> To Go Home </br>
		</div>
		
		<script src="webjars/jquery/3.5.1/jquery.min.js"></script>
		<script src="webjars/bootstrap/4.5.3/js/bootstrap.min.js"></script>
	</div>
</body>
</html>