<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<!-- MessageBox -->
	<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript">
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveEditVendor.action" id="Form1">
	<input type="hidden" id="vendorId" value="<s:property value='vendorInfo.vendorId'/>"/>
	<input type="hidden" id="taxNo" value="<s:property value='vendorInfo.vendorTaxNo'/>"/>
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">查看供应商</div>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">供应商基本信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr >
		<td width="15%" align="right">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorCName'/>"/></td>
		<td width="15%" align="right">供应商英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEName'/>"/></td>
	</tr>
	<tr >
		<td  align="right">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorTaxNo'/>"/></td>
		<td  align="right">供应商账号:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorAccount'/>"/></td>
	</tr>
	<tr >
		<td  align="right">供应商开户银行中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorCBank'/>"/></td>
		<td  align="right">供应商开户银行英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEBank'/>"/></td>
	</tr>
	<tr >
		<td  align="right">供应商联系人:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorLinkman'/>"/></td>
		<td  align="right">供应商电话:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorPhone'/>"/></td>
	</tr>
	<tr >
		<td  align="right">供应商邮箱地址:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEmail'/>"/></td>
		<td  align="right">供应商地址:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorAddress'/>"/></td>
	</tr>
	<tr >
		<td  align="right">供应商类别:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3">
			<s:if test='vendorInfo.taxpayerType=="S"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
			<s:elseif test='vendorInfo.taxpayerType=="G"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="一般纳税人"/></s:elseif>
			<s:elseif test='vendorInfo.taxpayerType=="I"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="个体纳税人"/></s:elseif>
			<s:elseif test='vendorInfo.taxpayerType=="O"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="其他"/></s:elseif>
			<s:else></s:else>
		</td>
	</tr>
	</table>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">供应商快递信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr >
		<td width="23%" align="right">收件人:&nbsp;&nbsp;&nbsp;</td>
		<td width="27%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addressee'/>"/></td>
		<td width="23%" align="right">收件人电话:&nbsp;&nbsp;&nbsp;</td>
		<td width="27%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseePhone'/>"/></td>
	</tr>
	<tr >
		<td  align="right">收件人邮编:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeZipcode'/>"/></td>
		<td  align="right">收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td ><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeAddress'/>"/></td>
	</tr>
	<tr >
		<td  align="right">详细收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeAddressdetail'/>"/></td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close()" />
	</div>
	<script language="javascript" type="text/javascript">
	</script>
	</form>
</body>
</html>