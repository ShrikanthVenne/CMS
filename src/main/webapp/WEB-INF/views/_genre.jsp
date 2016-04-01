<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<body>
	
		<table id="genreTable" class="table table-bordered">
			<thead>
				<tr>
					<th>GENRE_ID</th>
					<th>GENRE_NAME</th>
					<th>CATEGORY_NAME</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${genres}" var="genre">
					<tr>
						<td>${genre.genreId}</td>
						<td>${genre.genreName}</td>
						<td>${genre.category.category_name}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>