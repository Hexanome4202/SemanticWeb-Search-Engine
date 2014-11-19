<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="h4202.view.BeaverBeverGo,h4202.module2.Triplet,h4202.Similarity"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BeaverBeverGo</title>
</head>
<body>
	<table cellpadding="0px" cellspacing="0px">
		<tr>
			<td><img src="Beaver.jpg" alt="beaver_logo"></td>
			<td><h1 style="color: blue; text-align: center">BeaverBeverGo</h1></td>

			<form method="get" action="">

				<td
					style="border-style: solid none solid solid; border-color: #4B7B9F; border-width: 1px;">
					<input type="text" name="zoom_query"
					style="width: 100px; border: 0px solid; height: 17px; padding: 0px 3px; position: relative;">
				</td>
				<td
					style="border-style: solid; border-color: #4B7B9F; border-width: 1px;">
					<input type="submit" value=""
					style="border-style: none; width: 24px; height: 20px;">
				</td>
		</tr>
		<tr>
		
			<%
				Similarity sim = new Similarity();
				BeaverBeverGo bv = new BeaverBeverGo();
				sim.readAll();
				sim.fillSimilarityList();
				String img = bv.searchForPredicate(sim.getMapFiles(), BeaverBeverGo.IMAGE);
				
			%>
			
			<td><img src="<%out.println(img);%>" alt="" height="150" width="150"></td>
			<td><p><% out.println(bv.searchForPredicate(sim.getMapFiles(), BeaverBeverGo.LABEL)); %></p></td>
		</tr>
	</table>
	</form>

</body>
</html>