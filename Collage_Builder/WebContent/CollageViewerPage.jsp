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
		<%
		Collage main = new Collage();
		main.setTopic("Ming");
		main.setImage("http://www-scf.usc.edu/~csci201/images/ming_chen.jpg");

		Collage one = new Collage();
		one.setTopic("Miller");
		one.setImage("http://www-scf.usc.edu/~csci201/images/nikita_pashintsev.jpg");

		Collage two = new Collage();
		two.setTopic("Suvir");
		two.setImage("http://www-scf.usc.edu/~csci201/images/nikita_pashintsev.jpg");

		Collage three = new Collage();
		three.setTopic("Scott");
		three.setImage("http://www-scf.usc.edu/~csci201/images/nikita_pashintsev.jpg");

		ArrayList<Collage> previous = new ArrayList<Collage>();
		previous.add(one);
		previous.add(two);
		previous.add(three);
		%>
		<script>
			function switchCollage(elem) {
				var xhttp = new XMLHttpRequest();
				var switchCollages = "SwitchCollage.jsp?";

				buttonsPressed += "index=" + elem.id;

				xhttp.open("GET", buttonsPressed, false);
	  	  		xhttp.send();
	  	  		document.getElementById("entirePage").innerHTML = xhttp.responseText;
			}
		</script>
		<script>
			//function to send the topic to the back end and build the collage then send the user to the next page
			function buildCollage()
			{
	        		var xhttp = new XMLHttpRequest();
	        		var url = "MainController?topic="+document.getElementById("topic").value+"&first=false";
	            	xhttp.open("GET", url, true);
	            	xhttp.send();
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
				<!-- form that holds the export button -->
				<a href=<%=mainCollage.getImage()%> download>
				  	<form class="ExportForm">
						<input type="button" class="buttons" name="Export" value="Export">
					</form>
				</a>

				<!-- form that holds the build another collage inputs including the text field and the build another collage button -->
				<form class="BuildAnotherCollageForm">
					<input type="text" name="topic" placeholder="Enter Topic" oninput="IsEmpty()"  disable onsubmit="buildCollage()">
					<input type="submit" class="buttons" value="Build Collage">
				</form>
			</div>
			<!-- Div to hold the previos collage picker with divs to hold each image -->
			<div id="container" > <!--  onClick = "changeImage(event)"-->
			<%
			for(int i =0; i<previousCollage.size(); i++){ %>
				 <div id=<%=i %> onclick="switchCollage(this)"><img  src="data:image/png;base64,<%=previousCollage.get(i).getImage()%>" width="100%" height="100%" alt="Image Text" /></div>
			<%} %>
			</div>
			</div>
				<script type = "text/javascript">
					// function to switch images when clicked on in the previous collage viewer
					function changeImage(event){

						event = event || window.event;

						var targetElement = event.target || event.srcElement;

						if (targetElement == "IMG"){
						    var old = document.getElementByID("mainCollage").src
						    document.getElementByID("mainCollage").src = targetElement.getAttribute("src");
						    targetElement.setAttribute("src", old );
						}

					}
				</script>



			</div>

		</body>
	</html>
