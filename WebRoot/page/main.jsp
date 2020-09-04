<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
<html>
	<head>
		<TITLE>外汇管理局国际收支数据申报系统</TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
	</head>
	<frameset rows="77,*" cols="*" framespacing="0" frameborder="0" border="0">
		<frame src="${webapp}/page/header.jsp" name="topFrame" scrolling="no" noresize frameborder="0" marginwidth="0">
		<frameset rows="*" cols="140,5,*" framespacing="1" frameborder="0" id="frame" bordercolor="#daedfd">
			<frame name="leftFrame" src="${webapp}/menu.action" frameborder="0" scrolling="no" noresize id="leftFrame" style="border:1px solid #91b6df;">
			<frame name="midFrame" src="http://loacalhost:8090/bop/page/midlle.jsp" frameborder="0" scrolling="no" noresize id="midFrame"
				style=" solid #91b6df;">
			<frame name="sysmain" src="${webapp}/page/welcome.jsp" frameborder="0" scrolling="auto"
				id="sysmain" style="border:1px solid #91b6df;" marginheight="0">
		</frameset>
	</frameset>
</html>
