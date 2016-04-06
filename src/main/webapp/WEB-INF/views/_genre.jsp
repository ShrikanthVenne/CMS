<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<body>
	
		<table id="genreTable" class="table table-bordered">
			<thead>
				<tr>
					<th class="text-center">ID</th>
					<th class="text-center">NAME</th>
					<th class="text-center">CATEGORY</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${genres}" var="genre">
					<tr>
						<td class="text-center">${genre.genreId}</td>
						<td class="text-center">${genre.genreName}</td>
						<td class="text-center">${genre.category.category_name}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>