<%@ page language="java" contentType="text/html; charset=UTF-16" pageEncoding="UTF-16"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
<title>Insert title here</title>
</head>
<body>
	<table width="100%" height="100%">
		<tr>
			<td><jsp:include page="menu.jsp"></jsp:include></td>
		</tr>
		<tr>
			<td><jsp:include page="${partial}" /></td>
		</tr>
	</table>
</body>
</html>