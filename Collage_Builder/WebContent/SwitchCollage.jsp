<%@ page import="server.*" %>
<%@ page import = "javax.servlet.RequestDispatcher" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "javax.servlet.http.HttpSession" %>

<%
	//this code switches the collage object that was selected with the one in the main collage space
	String index = request.getParameter("index");

	ArrayList<Collage> previousCollage = (ArrayList<Collage>) session.getAttribute("PreviousCollageList");
	Collage mainCollage= (Collage) session.getAttribute("MainCollage");
	
	Collage temp = previousCollage.get(Integer.parseInt(index));
	previousCollage.set(Integer.parseInt(index),mainCollage);
	mainCollage = temp;
	
	session.setAttribute("PreviousCollageList", previousCollage);
	session.setAttribute("MainCollage", mainCollage);
%>
			<!-- this is the inner html that is retruned after the switch -->
			<!-- Title at top of the page -->
			<h1>Collage For Topic <%= mainCollage.getTopic() %></h1>
			<!-- Div to hold the main collage viewing area -->
			<div class="MainCollageView">
				<!-- Div to hold image that populates the main collage viewer area -->
				<div id="mainCollageSpace"><img onclick="exb()" id="mainCollage" src="data:image/png;charset=utf-8;base64,<%=mainCollage.getImage() %>" width="100%" height="100%" alt="Iage Text" /></div>
			</div>
			<!-- Div to hold all of the buttons and input fields -->
			<div class="Inputs">
				<!-- form that holds the export button -->
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
			<div id="container" > <!--  onClick = "changeImage(event)"-->
			<%for(int i =0; i<previousCollage.size(); i++){ %>
				 <div id=<%=i %> onclick="switchCollage(this)"><img  src="data:image/png;charset=utf-8;base64,<%=previousCollage.get(i).getImage()%>" width="100%" height="100%" alt="Image Text" /></div>
			<%} %>
			</div>