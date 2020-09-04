<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@ include file="../../page/modalPage.jsp"%>
<%@ include file="../../page/include.jsp"%>
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
<body onload="load()">
<div class="showBoxDiv">
<form name="detailForm" id="detailForm"  method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<input type="hidden" name="billId" value="<s:property value='billId'/>" />
	<tr>
		<td width="20%" align="right" class="listbar">发票种类:</td>
		<td><s:property value='inputInvoice.faPiaoType'/></td>
		<td width="20%" align="right" class="listbar">开票日期:</td>
		<td width="35%"><s:property value='inputInvoice.billDate'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">发票号码:</td>
		<td><s:property value='inputInvoice.billNo'/></td>
		<td width="20%" align="right" class="listbar">发票代码:</td>
		<td width="35%"><s:property value='inputInvoice.billCode'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">购方纳税人名称:</td>
		<td><s:property value='inputInvoice.name'/></td>
		<td width="20%" align="right" class="listbar">购方纳税人识别号:</td>
		<td width="35%"><s:property value='inputInvoice.taxNo'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">购方地址电话:</td>
		<td width="35%"><s:property value='inputInvoice.addressAndPhone'/></td>		
		<td width="20%" align="right" class="listbar">购方银行账号:</td>
		<td width="35%"><s:property value='inputInvoice.bankAndAccount'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">合计金额:</td>
		<td width="35%"><s:property value='inputInvoice.amtSum'/></td>
		<td width="20%" align="right" class="listbar">合计税额:</td>
		<td><s:property value='inputInvoice.taxAmtSum'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">价税合计:</td>
		<td><s:property value='inputInvoice.sumAmt'/></td>
		
		<td width="20%" align="right" class="listbar">备注:</td>
		<td><s:property value='inputInvoice.remark'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">开票人:</td>
		<td width="35%"><s:property value='inputInvoice.drawer'/></td>
		<td width="20%" align="right" class="listbar">复核人:</td>
		<td width="35%"><s:property value='inputInvoice.reivewer'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">收款人:</td>
		<td width="35%"><s:property value='inputInvoice.payee'/></td>
		<td width="20%" align="right" class="listbar">销方纳税人名称:</td>
		<td width="35%"><s:property value='inputInvoice.vendorName'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">销方纳税人识别号:</td>
		<td width="35%"><s:property value='inputInvoice.vendorTaxNo'/></td>
		<td width="20%" align="right" class="listbar">销方地址电话:</td>
		<td width="35%"><s:property value='inputInvoice.vendorAddressAndPhone'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">销方银行账号:</td>
		<td width="35%"><s:property value='inputInvoice.vendorBankAndAccount'/></td>
		<td width="20%" align="right" class="listbar">所属机构:</td>
		<td width="35%"><s:property value='inputInvoice.instCode'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">通知单编号:</td>
		<td width="35%"><s:property value='inputInvoice.noticeNo'/></td>
		<td width="20%" align="right" class="listbar">状态:</td>
		<td width="35%">
			<s:if test='inputInvoice.dataStatus =="1"'>已扫描未认证</s:if><s:else></s:else>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">操作说明:</td>
		<td width="35%"><s:property value='inputInvoice.description'/></td>
		<td width="20%" align="right" class="listbar">转出比例:</td>
		<td width="35%"><s:property value='inputInvoice.vatOutProportion'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">转出金额:</td>
		<td width="35%"><s:property value='inputInvoice.vatOutAmt'/></td>
		<td width="20%" align="right" class="listbar">是否勾稽:</td>
		<td width="35%">
			<s:if test='inputInvoice.conformFlg =="1"'>勾稽</s:if><s:else>不勾稽</s:else>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">发票余额:</td>
		<td width="35%"><s:property value='inputInvoice.balance'/></td>
		<td width="20%" align="right" class="listbar">发票类型:</td>
		<td width="35%">
			<s:if test='inputInvoice.faPiaoType =="0"'>增值税专用发票</s:if><s:else>增值税普通发票</s:else>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">扫描日期:</td>
		<td width="35%"><s:property value='inputInvoice.scanDate'/></td>
		<td width="20%" align="right" class="listbar">扫描人:</td>
		<td width="35%"><s:property value='inputInvoice.scanOperator'/></td>
	</tr>
	<tr>
		<td width="20%" align="right" class="listbar">认证通过日期:</td>
		<td width="35%"><s:property value='inputInvoice.identifyDate'/></td>
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