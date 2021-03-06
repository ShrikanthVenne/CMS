<%@ page language="java" contentType="text/html; charset=UTF-16" pageEncoding="UTF-16"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/main.css">
<link rel="stylesheet" href="<c:url value='resources/css/jquery-ui.css'/>">
<script src="<c:url value="resources/js/jquery.js"/>"></script>
<script src="<c:url value="resources/js/jquery-ui.js"/>"></script>
<script src="<c:url value="resources/js/editcontent.js"/>"></script>
<title>Edit Content</title>
<style type="text/css">
.table-borderless tbody tr td, .table-borderless tbody tr th, .table-borderless thead tr th {
	border: none;
}
</style>
</head>
<body style="height: 100%">
	<div class="container">
		<h3>Edit Content</h3>
		<div>
			<table class="table table-borderless">
				<tr>
					<td><label>Category Name</label></td>
					<td><label>Content Name Search</label></td>
					<td><label>Uploaded From Date</label></td>
					<td><label>Uploaded To Date</label></td>
				</tr>
				<tr>
					<td style="padding: 5px;"><select name="categoryId" id="categories" style="width: 200px;" class="form-control">
							<c:forEach items="${category }" var="item">
								<option value="${item.category_id }"><c:out value="${item.category_name }"></c:out>
								</option>
							</c:forEach>
					</select></td>
					<td><input type="text" name="search" id="searchText" style="width: 200px;" class=" form-control"></td>
					<td><input type="text" name="fromUploadedDate" value="" id="fromUploadedDateId" class="form-control " readonly="readonly" style="width: 200px;"></td>
					<td><input type="text" name="toUploadedDate" value="" id="toUploadedDateId" class="form-control " readonly="readonly" style="width: 200px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="button" value="Get Contents" id="getContentsButton" class="btn btn-default"></td>
					<td colspan="2" align="left"><input type="button" value="Reset" id="resetButton" class="btn btn-default"></td>
				</tr>
			</table>
		</div>
		<div id="contentsList" align="center"></div>
	</div>
	<div class="modal" style="display: none">
		<div class="center">
			<img alt="" src="resources/images/loading_spinner.gif" />
		</div>
	</div>
</body>
</html>