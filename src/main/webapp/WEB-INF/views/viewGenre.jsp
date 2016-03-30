<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Categories</title>

<link rel="stylesheet" href="resources/css/bootstrap-multiselect.css">
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div class="container">
		<h3>Genre List</h3>
		<br/>
		<div class="form-group">
			<form action="" class="form-horizontal">
	        	<label class="control-label col-sm-2">Categories</label>        
	       		<select id="categories" multiple="multiple" class="col-sm-10">
					<c:forEach items="${categories}" var="category">
						<option value="${category.category_id}">${category.category_name}</option>				
					</c:forEach>
				</select>
			</form>        	
	    </div>
		
		<%-- <table class="table table-bordered">
			<thead>
				<tr>
					<th>Id</th>
					<th>Name</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${categories}" var="category">
					<tr>
						<td>${category.category_id}</td>
						<td>${category.category_name}</td>
					</tr>
				</c:forEach>
			</tbody>		
		</table>	 --%>
	</div>
<script src="resources/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#categories').multiselect({ 
	         includeSelectAllOption: true,
	         enableFiltering:true,        
	         enableCaseInsensitiveFiltering:true	        	         
	     });
		
		$('#categories').on('change', function(){
			var cats = $('#categories option:selected');
            var selected = [];
            $(cats).each(function(index, brand){
                selected.push($(this).val());
            });
            console.log(selected);
		});
	});	
	
	
</script>	
</body>
</html>