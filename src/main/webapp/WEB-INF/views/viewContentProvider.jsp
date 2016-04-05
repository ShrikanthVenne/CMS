<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Content Provider</title>
</head>
<body>	
	<div class="container">
		<h3>CP List</h3>
		<table class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th class="col-md-1">CP_ID</th>
					<th class="col-md-2">CP_NAME</th>
					<th class="col-md-1">COMPANY_NAME</th>
					<th class="col-md-2">ADDRESS</th>
					<th class="col-md-1">CONTRACT_START_DATE</th>
					<th class="col-md-2">CONTRACT_END_DATE</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cps}" var="cp">
					<tr>
						<td>${cp.id}</td>
						<td>${cp.name}</td>
						<td>${cp.companyName}</td>
						<td>${cp.address}</td>
						<td><fmt:formatDate pattern="dd/MM/yyyy" value="${cp.contractStartDate}" /></td>
						<td><fmt:formatDate pattern="dd/MM/yyyy" value="${cp.contractEndDate}" /></td>
					</tr>
				</c:forEach>
			</tbody>		
		</table>	
	</div>
		
</body>
</html>