<!DOCTYPE html>
	<html>
	<npm install save-svg-as-png>
		<head>
			<meta charset="UTF-8">
			<title>Collage Viewer Page</title>
			<link rel="stylesheet" href="CollageViewerPage.css">
		</head>
		<script>
			function doSomething(elem) {
				alert ('The ID of the element which triggered this is: ' + elem.id);
			}
		</script>
		<body>
			<div>
				<!-- Title at top of the page -->
				<h1>Collage For Topic X</h1>
				<!-- Div to hold the main collage viewing area -->
			<div class="MainCollageView">
				<!-- DI\iv to hold image that populates the main collage viewer area -->
				<div id="mainCollageSpace"><img onclick="exb()" id="mainCollage" src="some_image" width="99" height="36" alt="Iage Text" /></div>
			</div>
			<!-- Div to hold all of the buttons and input fields -->
			<div class="Inputs">
				<!-- form that holds the export button -->
				<form class="ExportForm">
					<input type="button" class="buttons" name="Export" value="Export">
				</form>
				<!-- form that holds the build another collage inputs including the text field and the build another collage button -->
				<form class="BuildAnotherCollageForm">
					<input type="text" placeholder="Enter Topic">
					<input type="submit" class="buttons" value="Build Collage">
				</form>
			</div>
			<!-- Div to hold the previos collage picker with divs to hold each image -->
			<div id="container" onClick = "changeImage(event)">
				 <div id="1" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="2" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="3" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="4" onclick="doSomething(this)"><img src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div>Div 5<br/>http://coursesweb.net/html/</div>
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
			</div>
			
		</body>
	</html>