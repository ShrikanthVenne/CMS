<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width, initial-scale=1" name="viewport">
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/main.css">
<script src="<c:url value="resources/js/jquery.js"/>"></script>
<script src="resources/js/jquery.validate.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<!--           <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar"> -->
			<!--             <span class="sr-only">Toggle navigation</span> -->
			<!--             <span class="icon-bar"></span> -->
			<!--             <span class="icon-bar"></span> -->
			<!--             <span class="icon-bar"></span> -->
			<!--           </button> -->
			<a class="navbar-brand" href="#">CMS</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li id="home"><a href="home">Home</a></li>
				<li id="uploadContent" class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Upload Content<span
						class="caret"
					></span></a>
					<ul class="dropdown-menu">
						<li><a href="bulkUpload">Bulk Upload</a></li>
						<li><a href="bulkLangUpload">Bulk Language Upload</a></li>
						<li><a href="#">Single Upload</a></li>
<!-- 						<li><a href="editContent">Edit content</a></li> -->
					</ul>
			     </li>
			     <li id="masters" class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Masters<span
						class="caret"
					></span></a>
					<ul class="dropdown-menu">
						<li><a href="viewCategory">Category</a></li>
						<li><a href="viewGenre">Genre</a></li>	
						<li><a href="viewContentProvider">CP</a></li>					
					</ul>
			     </li>
			</ul>
			<!--           <ul class="nav navbar-nav navbar-right"> -->
			<!--             <li><a href="../navbar/">Default</a></li> -->
			<!--             <li><a href="../navbar-static-top/">Static top</a></li> -->
			<!--             <li class="active"><a href="./">Fixed top <span class="sr-only">(current)</span></a></li> -->
			<!--           </ul> -->
		</div>
		<!--/.nav-collapse -->
	</div>
	</nav>
	<!--     <div class="container"> -->
	<!--       Main component for a primary marketing message or call to action -->
	<!--       <div class="jumbotron"> -->
	<!--         <h1>Navbar example</h1> -->
	<!--         <p>This example is a quick exercise to illustrate how the default, static and fixed to top navbar work. It includes the responsive CSS and HTML, so it also adapts to your viewport and device.</p> -->
	<!--         <p>To see the difference between static and fixed top navbars, just scroll.</p> -->
	<!--         <p> -->
	<!--           <a class="btn btn-lg btn-primary" href="../../components/#navbar" role="button">View navbar docs &raquo;</a> -->
	<!--         </p> -->
	<!--       </div> -->
	<!--     </div> /container -->
</body>
</html>