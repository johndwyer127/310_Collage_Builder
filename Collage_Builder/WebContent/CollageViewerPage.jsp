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
				alert ('The ID of the element which triggered this is: ' + elem.id;
			}
		</script>
		<body>
			<div>
				<h1>Collage For Topic X</h1>
			<div class="MainCollageView">
				<div id="mainCollageSpace"><img onclick="exb()" id="mainCollage" src="some_image" width="99" height="36" alt="Iage Text" /></div>
			</div>
			<div class="Inputs">
				<form class="ExportForm">
					<input type="button" onclick="exp()" class="buttons" name="Export" value="Export">
				</form>
				<form class="BuildAnotherCollageForm">
					<input type="text" placeholder="Enter Topic">
					<input type="submit" class="buttons" value="Build Collage">
				</form>
			</div>
			<!-- <div class="PreviousCollageViewer">
				
			</div> -->
			<div id="container" onClick = "changeImage(event)">
				 <div id="1" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="2" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="3" onclick="doSomething(this)"><img  src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div id="4" onclick="doSomething(this)"><img src="some_image" width="99" height="36" alt="Image Text" /></div>
				 <div>Div 5<br/>http://coursesweb.net/html/</div>
			</div>
				<script type = "text/javascript">
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