<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Genres</title>
<link rel="stylesheet" href="resources/css/bootstrap-multiselect.css">
<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body>
	<div class="container">
		<h3>Genre List</h3>
		<br/>
		
		<form action="" class="form-inline" id="catForm">
			<div class="form-group">
	        	<label >Categories:</label>  
	        	<span>
		        	<select id="categories" name="categories" multiple="multiple" >
						<c:forEach items="${categories}" var="category">
							<option value="${category.category_id}">${category.category_name}</option>				
						</c:forEach>
					</select>
					<label id="categoryLabel" style="display: none; color:red">Category is required</label>
				</span> 	        	   	       												
			</div>
			<br/><br/>
			<div>
				<button type="button" class="btn btn-default btn-sm" onclick="getGenres()">
		          <span class="glyphicon glyphicon-list"></span> List Genres
		        </button>	
			</div>															  									
		</form>        	
	    
	    <br/><br/>
	    <div id="genre">
	    	
	    </div>	
	</div>
<script src="resources/js/bootstrap-multiselect.js"></script>
<script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.min.js"></script>
<script src="resources/js/genre.js"></script>	
</body>
</html>