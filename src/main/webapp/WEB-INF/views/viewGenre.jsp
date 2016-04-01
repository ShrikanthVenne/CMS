<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Genres</title>

<link rel="stylesheet" href="resources/css/bootstrap-multiselect.css">
</head>
<body>
	<div class="container">
		<h3>Genre List</h3>
		<br/>
		<div class="form-group">
			<form action="" class="form-horizontal" id="catForm">
	        	<label class="control-label col-sm-2">Categories</label>   
	        	<div class="col-sm-2">     
		       		<select id="categories" name="categories" multiple="multiple" class="">
						<c:forEach items="${categories}" var="category">
							<option value="${category.category_id}">${category.category_name}</option>				
						</c:forEach>
					</select>
					<label id="categoryLabel" style="display: none; color:red">Category is required</label>					
				</div>
				&nbsp;	
				<button type="button" class="btn btn-default btn-sm col-sm" onclick="getGenres()">
		          <span class="glyphicon glyphicon-list"></span> List Genres
		        </button>									
			</form>        	
	    </div>
	    <br/><br/>
	    <div id="genre">
	    	
	    </div>	
	</div>
<script src="resources/js/bootstrap-multiselect.js"></script>
<script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.min.js"></script>
		
<script type="text/javascript">
    var selectedCategories = [];
	$(document).ready(function(){
		$('#categories').multiselect({ 
	         includeSelectAllOption: true,
	         enableFiltering:true,        
	         enableCaseInsensitiveFiltering:true	        	         
	     });
		
		 $("#categories").multiselect('selectAll', false);
		 $("#categories").multiselect('updateButtonText');
		
	});	

	function getGenres(){
		
		
		if($('#categories').val() != null){
			$('#categoryLabel').hide();
			 $.ajax({
				 url: "${pageContext.request.contextPath}/getGenreFromCategory",
				 data:{
					 categories:$('#categories').val()
				 },
				 success: function(result){
			        $('#genre').html(result);
			        $('#genreTable').dataTable();
			     },
			     error: function(error){
				   
				 }
			 });
		}
		else{
			$('#categoryLabel').show();
		}
		
	}
	
</script>	
</body>
</html>