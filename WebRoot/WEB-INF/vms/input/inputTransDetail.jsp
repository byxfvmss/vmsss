<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<!-- <title><s:if test="user.userId != null && user.userId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></title> -->
	<title>进项税信息明细</title>
	<!-- <link type="text/css" href="<c:out value="${bopTheme}"/>/css/subWindow.css" rel="stylesheet"> -->
	<link href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/inputInvoice.css" rel="stylesheet">

</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="detailForm" id="detailForm"  method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<input type="hidden" name="id" value="<s:property value='id'/>" />
	<tr>
		<th colspan="4">进项税导入数据明细</th>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">交易编号:&nbsp;&nbsp;&nbsp;</td>
		<td><s:property value='inputTrans.dealNo'/></td>
		<td width="20%" align="right" class="listbar">交易发生机构:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.bankCode'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">备注:&nbsp;&nbsp;&nbsp;</td>
		<td><s:property value='inputTrans.remark'/></td>
		<td width="20%" align="right" class="listbar">发票代码:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.billCode'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">发票号码:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.billNo'/></td>
		<td width="20%" align="right" class="listbar">金额_人民币:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.amtCny'/></td>
	</tr>
	<tr>
	  <td width="20%" align="right" class="listbar">税额_人民币:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.taxAmtCny'/></td>
		<td width="20%" align="right" class="listbar">支出_人民币:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.incomeCny'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">附加税1（城市建设）:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.surtax1AmtCny'/></td>
		<td width="20%" align="right" class="listbar">附加税2（教育附加）:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.surtax2AmtCny'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">附加税3（地方教育附加）:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.surtax3AmtCny'/></td>
		<td width="20%" align="right" class="listbar">附加税4（其他）:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.surtax4AmtCny'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">供应商ID:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.vendorId'/></td>
		<td width="20%" align="right" class="listbar">交易日期:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><s:property value='inputTrans.transDate'/></td>
	</tr>
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
</form>
</div>
</body>
</html>