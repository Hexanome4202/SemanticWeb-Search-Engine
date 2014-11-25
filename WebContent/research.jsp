<%@page import="h4202.model.ResultModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeSet"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BeaverBeverGo</title>
</head>
<body>
	<div style="background-color: #E8E8E8; margin: -10px;">
		<table align="center">
			<tr>
				<td><img
					src="http://images.frandroid.com/wp-content/uploads/2014/01/bridgestone.jpg"
					style="border-style: ridge; border-width: 5px; border-color: grey; width: 100px; height: auto;"
					alt="beaver_logo"></td>
				<td>
					<h1
						style="color: green; text-align: center; padding: 25px 25px 25px 25px;">
						BeaverBeverGo</h1>
				</td>

				<td>
					<table align="center">
						<tr>
							<form method="get" action="./ActionServlet">
								<input type="hidden" name="todo" value="search">
								<td
									style="border-style: solid none solid solid; border-color: green; border-width: 1px;">
									<input type="text" name="keyWords" id="keyWords"
									value="<%out.print(session.getAttribute("keyWords"));%>"
									style="width: 200px; border: 0px solid; height: 20px;">
								</td>
								<td
									style="border-style: solid; border-color: green; border-width: 1px; background-color: green;">
									<input type="image" value=""
									style="border-style: none; width: 24px; height: 20px;"
									src="http://www.clker.com/cliparts/v/C/2/9/I/6/search-icon-white-md.png">
								</td>
							</form>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<table>
		<%
			List<ResultModel> list = (List<ResultModel>) session
					.getAttribute("resultsList");
			for (ResultModel s : list) {
		%>
		<tr>

			<td><a href="<%out.print(s.getUrl());%>"><img src="<%out.print(s.getImgURL());%>" alt=""
				height="auto" width="150" ></a></td>
			<td style="text-align: left; vertical-align: top; padding: 20px;"></a>
				<table>
					<tr>
						<h2>
							<%
								out.print(s.getLabel());
							%>
						</h2>
					</tr>
					<tr>
						<p style= "font-size:small; font-family:arial,sans-serif">
							<%
								out.print(s.getDescription());
							%>
						</p>
					</tr>
					<tr>
						<%
							if (s.getWikipediaLink() != "") {
						%>
						<td><a href="<%out.print(s.getWikipediaLink());%>"><img
								alt="Wikipedia" height="auto" width="50"
								src="http://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/W-circle.svg/38px-W-circle.svg.png" /></a></td>
						<%
							}
						%>
						<%
							if (s.getHomePageLink() != "") {
						%>
						<td><a href="<%out.print(s.getHomePageLink());%>"><img
								alt="HomePage" height="auto" width="40"
								src="https://cdn1.iconfinder.com/data/icons/MetroStation-PNG/128/MB__home.png" /></a></td>
						<%
							}
						%>
					</tr>
				</table>
			</td>
		</tr>
		<%
			}
		%>
	</table>

</body>
</html>