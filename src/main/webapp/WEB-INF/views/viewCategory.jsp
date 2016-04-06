<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Categories</title>
</head>
<body>	
	<div class="container">
		<h3>Category List</h3>
		<table class="table table-bordered table-nonfluid">
			<thead>
				<tr>
					<th class="col-md-1 text-center" >ID</th>
					<th class="col-md-2 text-center">NAME</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${categories}" var="category">
					<tr>
						<td class="text-center">${category.category_id}</td>
						<td class="text-center">${category.category_name}</td>
					</tr>
				</c:forEach>
			</tbody>		
		</table>	
	</div>
		
</body>
</html>