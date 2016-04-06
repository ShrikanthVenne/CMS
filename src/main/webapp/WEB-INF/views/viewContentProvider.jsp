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
					<th class="text-center">ID</th>
					<th class="text-center">NAME</th>
					<th class="text-center">COMPANY</th>
					<th class="text-center">ADDRESS</th>
					<th class="text-center">CONTRACT START DATE</th>
					<th class="text-center">CONTRACT END DATE</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cps}" var="cp">
					<tr>
						<td class="text-center">${cp.id}</td>
						<td class="text-center">${cp.name}</td>
						<td class="text-center">${cp.companyName}</td>
						<td class="text-center">${cp.address}</td>
						<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy" value="${cp.contractStartDate}" /></td>
						<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy" value="${cp.contractEndDate}" /></td>
					</tr>
				</c:forEach>
			</tbody>		
		</table>	
	</div>
		
</body>
</html>