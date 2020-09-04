<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@include file="include.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="<%=bopTheme%>/css/common.css" type="text/css"
			rel="stylesheet">
	</head>

	<body>
		<table class="location">
			<tr>
				<td>
					当前位置：系统错误=&gt;
					<span id="LblActionText">错误信息</span>
				</td>
			</tr>

			<tr>
				<td>
					<s:actionerror />
					<s:actionmessage />
				</td>
			</tr>
			<tr>
				<td>
					<s:property value="exception.message" />
				</td>
			</tr>
			<tr>
				<td>
					<s:property value="exceptionStack" />
				</td>
			</tr>
		</table>
	</body>
</html>