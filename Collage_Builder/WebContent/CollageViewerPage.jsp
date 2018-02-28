<!DOCTYPE html>
<%@ page import="server.*" %>
<%@ page import = "javax.servlet.RequestDispatcher" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "javax.servlet.http.HttpSession" %>
	<html>
	<% ArrayList<Collage> previousCollage= (ArrayList<Collage>) session.getAttribute("PreviousCollageList"); %>
	<% Collage mainCollage= (Collage) session.getAttribute("MainCollage");
	%>
		<head>
			<meta charset="UTF-8">
			<title>Collage Viewer Page</title>
			<link rel="stylesheet" href="CollageViewerPage.css">
		</head>
		<script>
			function switchCollage(elem) {
				var xhttp = new XMLHttpRequest();
				var switchCollages = "SwitchCollage.jsp?";
				switchCollages += "index=" + elem.id;
				xhttp.open("GET", switchCollages, false);
	  	  		xhttp.send();

	  	  		console.log(xhttp.responseText);
	  	  		document.getElementById("entirePage").innerHTML = xhttp.responseText;
			}
			//function to send the topic to the back end and build the collage then send the user to the next page
			function buildCollage()
			{
	        		var xhttp = new XMLHttpRequest();
	        		var url = "MainController?topic="+document.getElementById("topic").value+"&first=false";
	            	xhttp.open("GET", url, false);
	            	xhttp.send();
			}
			//checks if the text input is empty and enables the button if it is not
			function IsEmpty() {
				 if(document.getElementById("topic").value == "")
	             {
					 document.getElementById("submitButton").disabled = true;
	             }else{
	            	 	document.getElementById("submitButton").disabled = false;
	             }

			}
		</script>
		
		<body>
			<div id="entirePage">
				<!-- Title at top of the page -->
				<h1>Collage For Topic <%= mainCollage.getTopic() %></h1>
				<!-- Div to hold the main collage viewing area -->
				
				<div class="MainCollageView">
					<!-- Div to hold image that populates the main collage viewer area -->
					<img onclick="exb()" id="mainCollage" src="data:image/png;base64,<%=mainCollage.getImage()%>" width="100%" height="100%"/>
				</div>
				
				<!-- Div to hold all of the buttons and input fields -->
				<div class="Inputs">
					<!-- form that holds the export button and allows us to download the image-->
					<a href="data:image/png;base64,<%=mainCollage.getImage()%>" download="test.png">
					  	<form class="ExportForm">
							<input type="button" class="buttons" name="Export" value="Export Collage">
						</form>
					</a>
					<!-- form that holds the build another collage inputs including the text field and the build another collage button -->
					<form class="BuildAnotherCollageForm">
						<input type="text" name="topic" placeholder="Enter Topic" oninput="IsEmpty()"  disable onsubmit="buildCollage()">
						<input type="submit" class="buttons" value="Build Another Collage">
					</form>
				</div>
				
				<!-- Div to hold the previos collage picker with divs to hold each image -->
				<div id="container" >
					<%for(int i =0; i<previousCollage.size(); i++){%>
						 <div id=<%=i %> onclick="switchCollage(this)"><img  src="data:image/png;base64,<%=previousCollage.get(i).getImage()%>" width="100%" height="100%" alt="Image Text" /></div>
					<%}%>
				</div>
			</div>
		</body>
	</html>
