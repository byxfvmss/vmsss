<%@page import="com.cjit.vms.trans.model.storage.InvoiceStockDetail"%>
<%@page import="java.util.List"%>
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
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
<title>查看作废原因:</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>

</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="operType" value="<s:property value="operType" />" />
	<input type="hidden" id="paperInvoiceStockId" name="paperInvoiceStockId" value="<s:property value="paperinvoicestock.paperInvoiceStockId" />" /> 

<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="9">查看作废原因</th>
	</tr>
	<tr>
		<td style="text-align: left ;top: 20px; font-weight:bold ">作废原因：</td>
	</tr>
	<tr>
	<td >	
		<textarea readonly="readonly"  name="invalidReason" id="invalidReason" 
		cols="60" rows="9" size="200"  ><s:property value="note"/></textarea> 	
	</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" 
	onMouseMove="this.className='tbl_query_button_on'"
	 onMouseOut="this.className='tbl_query_button'"
	  onclick="CloseWindow();" name="BtnReturn"
	   value="返回" id="BtnReturn"/>
</div>
</div>
</form>
</div>
</body>

</html>