<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BeaverBeverGo</title>
</head>
<body style="margin-top:150px;">

	<table align="center" style="margin-bottom:35px;">
		<tr>
			<td> <img src="http://images.frandroid.com/wp-content/uploads/2014/01/bridgestone.jpg" style="border-style: ridge; border-width:5px; border-color:grey; width: 150px; height: auto;" alt="beaver_logo"> </td>
			<td> <h1 style="color: green; text-align: center; padding: 25px 25px 25px 25px; letter-spacing: 5px;"> BeaverBeverGo </h1> </td>
		</tr>
	</table>
	
	<form method="get" action="./ActionServlet">
	<input type="hidden" name="todo" value="search">
		<table align = "center">
			<tr>
				<td
					style="border-style: solid none solid solid; border-color: green; border-width: 1px;">
					<input type="text" name="keyWords" id="keyWords"
					style="width: 350px; border: 0px solid; height: 17px; position: relative;">
				</td>
				<td style="border-style: solid; border-color: green; border-width: 1px;  background-color: green;">
					<input type="image" value=""
					style="border-style: none; width: 24px; height: 20px;"
					src="http://www.clker.com/cliparts/v/C/2/9/I/6/search-icon-white-md.png">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>