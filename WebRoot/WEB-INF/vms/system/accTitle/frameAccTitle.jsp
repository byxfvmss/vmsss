<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>科目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"  />
</head>
	<frameset rows="45,*" cols="*" framespacing="0" frameborder="0" border="0">
		<frame src="frameHeadAccTitle.action" name="topFrame" scrolling="no" noresize frameborder="0" marginwidth="0">
		<frameset rows="*" cols="300,*" framespacing="0" frameborder="0" id="frame">
			<frame src="treeAccTitle.action" name="treeItems" id="leftFrame" frameborder="0" scrolling="no" noresize>
			<frame src="" name="listTransType" id="mainFrame" frameborder="0" scrolling="no" marginheight="0" marginwidth="0">
		</frameset>
	</frameset>
</html>
