<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<font color="red">${errorMsg }</font>
		<form method="post">
		User Name	:	<input type="text" name="userName"/></br>
		Password	:	<input type="text" name="password"/></br>
						<input type="submit" name="loginObj"/></br>
		</form>
</body>
</html>