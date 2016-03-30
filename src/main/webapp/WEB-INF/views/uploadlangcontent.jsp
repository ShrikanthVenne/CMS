
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div class="container">
		<h3>Upload Language Content</h3>
		<br />
		<form:form method="POST" action="doLangUpload" class="form-horizontal" role="form" id="uploadForm" enctype="multipart/form-data" modelAttribute="fileBO">
			<div class="form-group">
				<label class="control-label col-sm-2" for="file">Upload File</label>
				<div class="col-sm-10">
					<span class="btn btn-default btn-file"> <input type="file" name="file" id="file" />
					</span>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">
						<span class="glyphicon glyphicon-upload"></span> Upload
					</button>
				</div>
			</div>
		</form:form>
		<br /> <br />
		<c:if test="${error != null}">
			<div class="error">
				<h4>${error}</h4>
			</div>
		</c:if>
		<c:if test="${messages != null}">
			<div class="error">
				<h4>Error in Uploading</h4>
			</div>
			<br />
			<br />
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>Content Name</th>
						<th>Errors</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${messages}" var="message">
						<tr>
							<td>${message.key}</td>
							<td>
								<ul>
									<c:forEach items="${message.value}" var="errorMsg">
										<li>${errorMsg}</li>
									</c:forEach>
								</ul>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		<c:if test="${size > 0 }">
			Total ${size } contents uploaded.
		</c:if>
		<div class="modal" style="display: none">
			<div class="center">
				<img alt="" src="resources/images/loading_spinner.gif" />
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#uploadContent").addClass("active");
			$("#uploadForm").submit(function(event) {
				$(".modal").show();
			});
		});
	</script>
</body>
</html>