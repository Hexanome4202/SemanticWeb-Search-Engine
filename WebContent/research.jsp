<%@page import="h4202.model.ResultModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
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
	<h1>Results</h1>
	<table>
		<%
			String imgSrc = "";
			List<ResultModel> list = (List<ResultModel>) session.getAttribute("resultsList");
			List<String> categories;
			if(list.size() == 0) out.println("<p>We are deeply sorry but we didn't find anything interesting.<br /><i>You can still try to find some useful information using the links next to the graph below. If no results are displayed, try to be more specific next time :)</i><p>");
			for (ResultModel s : list) {
				categories = s.getCategories();
				imgSrc = (s.getImgURL() == null || "".equals(s.getImgURL())) ? 
						"http://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/300px-No_image_available.svg.png" : s.getImgURL();
		%>
		<tr>
			<td><a href="<%out.print(s.getUrl());%>"><img src="<%out.print(imgSrc);%>" alt=""
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
						<h3>Abstract</h3>
						<p style= "font-size:small; font-family:arial,sans-serif">
							<%
								out.print(s.getDescription());
							%>
						</p>
					</tr>
					<tr>
						<u>To read more:</u>
						<%
							if (!"".equals(s.getWikipediaLink())) {
						%>
						<td><a title="Wikipedia" href="<%out.print(s.getWikipediaLink());%>"><img
								alt="Wikipedia" height="auto" width="50"
								src="http://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/W-circle.svg/38px-W-circle.svg.png" /></a></td>
						<%
							}
						%>
						<%
							if (s.getHomePageLink() != "") {
						%>
						<td><a title="Home page" href="<%out.print(s.getHomePageLink());%>"><img
								alt="HomePage" height="auto" width="40"
								src="https://s3.amazonaws.com/saveoneverything_assets/assets/images/icons/home_services_icon.png" /></a></td>
						<%
							}
						%>
					</tr>
				</table>
			</td>
			<td>
				<table>
					<tr>
						<td>
							<h3>Categories</h3>
							<ul style="height:50px;overflow-y:scroll;">
								<%
								for (String cat : categories) {
									out.println("<li>" + cat + "</li>");
								}
								%>
							</ul>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<%
			}
		%>
	</table>
	<hr />
	<h1>Meaningful articles and link between them</h1>
	<div id="mynetwork" style="display: inline-block"></div>
	<ul style="display: inline-block; vertical-align: top; max-width:20%;">
		<%
			HashMap<String, Integer> map = (HashMap<String, Integer>) session.getAttribute("map");
			if(map.size() == 0) out.println("<p>Nothing to see here, move on!</p>");
			for(Map.Entry<String, Integer> entry: map.entrySet()) {
				out.println("<li>" + entry.getValue() + " --> <a href=\""+entry.getKey()+"\">" + entry.getKey() + "</a></li>");
			}
		%>
	</ul>
	<% out.println(session.getAttribute("viz")); %>
	<% out.println(session.getAttribute("graph")); %>
</body>
</html>