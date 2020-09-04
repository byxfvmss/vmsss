<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<%@include file="../../page/include.jsp"%>
	<title>业务明细</title>
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
</head>
<body>
<div class="showBoxDiv">
<form name="confirmPrintForm" id="confirmPrintForm" action="" method="post">
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			业务明细
		</th>
	</tr>
	<tr>
	<tr>
		<td class="contnettable-subtitle" colspan="10">业务基本信息</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">交易日期:</td>
		<td width="35%"><s:property value='#request.transDate' /></td>
		<td width="15%" style="text-align: right" class="listbar">交易类别:</td>
		<td width="35%"><s:property value='#request.businessCName' /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">交易金额:</td>
		<td width="35%"><s:property value='#request.amtCny' /></td>
		<td width="15%" style="text-align: right" class="listbar">税率:</td>
		<td width="35%"><s:property value='#request.taxRate'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">税额:</td>
		<td width="35%"><s:property value='#request.taxAmt' /></td>
		<td width="15%" style="text-align: right" class="listbar">价税合计:</td>
		<td width="35%"><s:property value='#request.sumAmt'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">交易机构:</td>
		<td width="35%"><s:property value='#request.branchCode'/></td>
		<td width="15%" style="text-align: right" class="listbar">交易部门:</td>
		<td width="35%"><s:property value='#request.departCode'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户编号:</td>
		<td width="35%"><s:property value='#request.customerCode' /></td>
		<td width="15%" style="text-align: right" class="listbar">客户名称:</td>
		<td width="35%"><s:property value='#request.customerCName'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户税号:</td>
		<td width="35%"><s:property value='#request.customerTaxNo' /></td>
		<td width="15%" style="text-align: right" class="listbar">客户类别:</td>
		<td width="35%"><s:property value='#request.taxpayerType'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户部门:</td>
		<td width="35%"><s:property value='#request.customerOffice' /></td>
		<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
		<td width="35%"><s:property value='#request.billCode'/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票号码:</td>
		<td width="35%"><s:property value='#request.billNo' /></td>
		<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
		<td width="35%">发票类型</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">状态:</td>
		<td width="35%"><s:property value='#request.dataStatus' /></td>
		<td width="15%" style="text-align: right" class="listbar">开票日期:</td>
		<td width="35%"><s:property value='#request.billDate'/></td>
	</tr>
	
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow()" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>
</form>
</div>
</body>
</html>