<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>导入数据列表 </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
	<frameset rows="65,*" cols="*" framespacing="0" frameborder="0" border="0">
		<frame src="listImpdataHead.action" name="topFrame" scrolling="no" noresize frameborder="0" marginwidth="0">
		<!-- <frameset rows="*" cols="0,*" framespacing="0" frameborder="0" id="frame">
			<frame src="listImpdataTree.action" name="InstTreeFrame" id="leftFrame" frameborder="0" scrolling="no" noresize>
			<frame src="listImpdataMain.action" name="InstSearchResultFrame" id="mainFrame" frameborder="0" scrolling="no" marginheight="0" marginwidth="0">
		</frameset> -->
		<frame src="listImpdataMain.action" name="InstSearchResultFrame" id="mainFrame" frameborder="0" scrolling="no" marginheight="0" marginwidth="0">
	</frameset>
</html>
