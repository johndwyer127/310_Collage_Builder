<!DOCTYPE html>
	<html>
		<script>
			function buildCollage() 
			{
    			window.location.href = "file:///Users/ScottKriesberg/Desktop/310_Collage_Builder/Collage_Builder/WebContent/CollageViewerPage.html";
			}
		</script>
		<head>
			<meta charset="UTF-8">
			<title>Initial Page</title>
			<link rel="stylesheet" href="InitialPage.css">
		</head>
		<body>
			<div class="formDiv">
				<form class="buildCollageForm" onsubmit="buildCollage()">
					<input type="text" class = "inputTextForm" placeholder="Enter Topic">
					<input type="submit" class = "buildCollageButton" value="Build Collage">
				</form>
			</div>
		</body>
	</html>