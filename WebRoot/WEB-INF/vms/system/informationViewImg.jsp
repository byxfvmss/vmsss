<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title>票据查看</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>

</head>
<body>
<div class="showBoxDiv">

<form id="frm" method="post">
<div style="overflow: auto; width: 100%; " >  
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
<!--	<tr class="header">-->
<!--		<th>票据查看</th>-->
<!--	</tr>-->
	<tr>
		<td>
		<s:if test='urlBillImage == ""'>指定票据图片不存在！</s:if><s:else><img src="<s:property value="urlBillImage"/>" width="770px"></s:else>
		</td>
	</tr>
</table>
</div>


<!--<div id="ctrlbutton" class="ctrlbutton" style="border:0px">-->
<!--	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>-->
<!--</div>-->

</form>
</div>
</body>

</html>