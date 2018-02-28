<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="server.*" %>
<%@ page import = "javax.servlet.RequestDispatcher" %>
	<html>
		<script>
			//function to send the topic to the back end and build the collage then send the user to the next page
			function buildCollage()
			{
				console.log("in the function");
	        		var xhttp = new XMLHttpRequest();
	        		var url = "MainController?topic="+document.getElementById("topic").value+"&first=true";
	            	xhttp.open("GET", url, true);
	            	xhttp.send();
	            	return true;
			}
			function IsEmpty() {
				 if(document.getElementById("topic").value == "")
	             {
					 document.getElementById("submitButton").disabled = true;
	             }else{
	            	 	document.getElementById("submitButton").disabled = false;
	             }

			}
		</script>
		<head>
			<meta charset="UTF-8">
			<title>Initial Page</title>
			<link rel="stylesheet" href="InitialPage.css">
		</head>
		<body>
			<div class="formDiv">
				<form method="post" action="${pageContext.request.contextPath}/MainController">
					<input type="text" id="topic" name="topic" class="inputTextForm" oninput="IsEmpty()" placeholder="Enter Topic">
					<input type="submit" id="submitButton" class="buildCollageButton" value="Build Collage" disabled>
				</form>
			</div>
		</body>
	</html>
