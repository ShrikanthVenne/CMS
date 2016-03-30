<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bulk Upload</title>
</head>
<body>
	<div class="container">
		<h3>Upload Content</h3>
		<br />
		<form:form method="POST" action="doUpload" class="form-horizontal" role="form" id="uploadForm" enctype="multipart/form-data" modelAttribute="fileBO">
			<div class="form-group">
				<label class="control-label col-sm-2">APP/WAP</label>
				<div class="col-sm-10">
					<label class="radio-inline"><input id="APP" type="radio" name="appOrWap" value="APP">APP</label> <label class="radio-inline"><input id="WAP" type="radio" name="appOrWap"
						value="WAP"
					>WAP</label> <label class="radio-inline"><input id="BOTH" type="radio" name="appOrWap" value="BOTH">Both</label>
				</div>
			</div>
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
			<input type="hidden" name="app" id="IsApp" value="true" />
			<input type="hidden" name="wap" id="IsWap" value="false" />
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
		<c:if test="${contentList != null}">
			<div class="success">
				<h4>Content Uploaded Successfully</h4>
			</div>
			<br />
			<div align="right" class="col-sm-offset-2 col-sm-10">
				<button id="btnExport" class="btn btn-default">
					<span class="glyphicon glyphicon-download-alt"></span>
				</button>
			</div>
			<br />
			<br />
			<div id="export">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>CONTENT_ID</th>
							<th>CONTENT_NAME</th>
							<th>CONTENT_CATEGORY</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${contentList}" var="content">
							<tr>
								<td>${content.content_id}</td>
								<td>${content.content_name}</td>
								<td>${content.category.category_name}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div style="display: none" id="inner"></div>
			</div>
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

			//Select APP by default
			$("#APP").attr('checked', 'checked');
			$("#IsAPP").val("true");

			$('input[type=radio][name=appOrWap]').change(function() {
				if (this.value == "APP") {
					$("#IsApp").val("true");
					$("#IsWap").val("false");
				} else if (this.value == "WAP") {
					$("#IsWap").val("true");
					$("#IsApp").val("false");
				} else {
					$("#IsApp").val("true");
					$("#IsWap").val("true");
				}
			});
			$("#uploadForm").submit(function(event) {
				$(".modal").show();
			});
		});
		$("#btnExport").click(function(e) {
			//creating a temporary HTML link element (they support setting file names)
			var a = document.createElement('a');
			//getting data from our div that contains the HTML table
			var data_type = 'data:application/vnd.ms-excel';
			var table_div = document.getElementById('export');
			var table_html = table_div.outerHTML.replace(/ /g, '%20');
			a.href = data_type + ', ' + table_html;

			//setting the file name
			a.download = 'exported_table' + '.xls';
			a.id = "exportLink";
			$("#inner").append(a);
			document.getElementById('exportLink').click();
			//just in case, prevent default behaviour
			e.preventDefault();
		});
	</script>
</body>
</html>