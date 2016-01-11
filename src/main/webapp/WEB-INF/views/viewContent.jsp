<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploaded Content</title>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div class="container">
		<h3>Content Uploaded Successfully</h3>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>CONTENT_ID</th>
					<th>CONTENT_NAME</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${contentList}" var="content">
					<tr>
						<td>${content.content_id}</td>
						<td>${content.content_name}</td>
					</tr>
				</c:forEach>
			</tbody>		
		</table>	
	</div>
		
</body>
</html>