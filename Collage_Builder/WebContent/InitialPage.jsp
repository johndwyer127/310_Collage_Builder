<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="server.*" %>
<%@ page import = "javax.servlet.RequestDispatcher" %>
	<html>
		<script>
			function buildCollage() 
			{
				console.log("in the function");
        		var xhttp = new XMLHttpRequest();
        		var url = "MainController?topic="+document.getElementById("topic").value;
            	xhttp.open("GET", url, true);
            	xhttp.send();
			}
		</script>
		<head>
			<meta charset="UTF-8">
			<title>Initial Page</title>
			<link rel="stylesheet" href="InitialPage.css">
		</head>
		<body>
			<div class="formDiv">
				<form class="buildCollageForm" method="GET" onsubmit="buildCollage()" action="CollageViewerPage.jsp">
					<input type="text" id="topic" name="topic" class="inputTextForm" placeholder="Enter Topic">
					<input type="submit" class = "buildCollageButton" value="Build Collage">
				</form>
			</div>
		</body>
	</html>