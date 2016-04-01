<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
	<div class="container">
		<h1>
			Welcome!  
		</h1>
		<script type="text/javascript">
			$( document ).ready(function() {
			    $("#home").addClass("active");
			});
		</script>
		<P>  The time on the server is ${serverTime}. </P>
	</div>
</body>
</html>
