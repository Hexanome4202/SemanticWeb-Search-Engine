<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BeaverBeverGo</title>
</head>
<body>
	<table>
		<tr>
			<td><img src="Beaver.jpg" alt="beaver_logo"></td>
			<td><h1 style="color: blue; text-align: center">BeaverBeverGo</h1></td>

			<form method="get" action="./ActionServlet">
			<input type="hidden" name="todo" value="search">
				<td
					style="border-style: solid none solid solid; border-color: #4B7B9F; border-width: 1px;">
					<input type="text" name="keyWords" id="keyWords" value="<% out.print(session.getAttribute("keyWords")); %>"
					style="width: 100px; border: 0px solid; height: 17px; padding: 0px 3px; position: relative;">
				</td>
				<td
					style="border-style: solid; border-color: #4B7B9F; border-width: 1px;"
					colspan="3"><input type="submit" value=""
					style="border-style: none; width: 24px; height: 20px;"></td>
			</form>
		</tr>
	</table>
	<table>
		<tr>

			<td><img src="<%out.print(session.getAttribute("img"));%>" alt="" height="150"
				width="150"></td>
			<td style="text-align: left; vertical-align: top; padding: 0">
				<table>
				<tr>
					<h2>
						<%
						out.print(session.getAttribute("label"));
						%>
					</h2>
				</tr>
				<tr>
					<p>
						<%
						out.print(session.getAttribute("description"));
						%>
					</p>
				</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>