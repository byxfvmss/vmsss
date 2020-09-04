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
<title>票据查看</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>

</head>
<body style="background:#FFF;" scroll="no">
<div class="showBoxDiv">

<form id="frm" method="post">
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">票据查看</div>
<table id="tbl_context" class="lessGrid" cellspacing="0"	width="100%" align="center" cellpadding="0">
	<tr>
		<td width="20%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票代码:</td>
		<td width="30%" style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.billCode" /></td>
		<td width="20%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票号码:</td>
		<td width="30%" style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.billNo" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票日期:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.billDate" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">价税合计金额:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.sumAmt" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商中文名称:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.vendorName" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商纳税人识别号:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.vendorTaxno" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商电话:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.vendorPhone" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商地址:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.vendorAddress" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计金额:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.amtSum" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计税额:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.taxAmtSum" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">收款人:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.payee" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商银行账号:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.vendorBankandaccount" /></td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">审核人:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.reviewer" /></td>
		<td style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票人:</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.drawer" /></td>
	</tr>
	<tr>
		<td width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">备注:</td>
		<td colspan="3" style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.remark" />
<!--			<textarea rows="3" cols="40" disabled="disabled"><s:property value="inputInvoiceInfo.remark" /></textarea>-->
		</td>
	</tr>
</table>
<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; padding-left:10px;">商品信息</div>
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr >
		<th style="text-align: center">商品名称</th>
		<th style="text-align: center">规格型号</th>
		<th style="text-align: center">商品数量</th>
		<th style="text-align: center">商品单价</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税率</th>
		<th style="text-align: center">税额</th>
	</tr>
	<s:if test='itemList == null || itemList.size()==0'>
	<tr><td colspan="7">该发票没有对应的商品信息！</td></tr>
	</s:if>
	<s:else>
	<s:iterator value="itemList" id="iList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td><s:property value='goodsName'/></td>
			<td><s:property value='specandmodel'/></td>
			<td><s:property value='goodsNo'/></td>
			<td><s:property value='goodsPrice'/></td>
			<td><s:property value='amt'/></td>
			<td><s:property value='taxRate'/></td>
			<td><s:property value='taxAmt'/></td>
		</tr>
	</s:iterator> 
	</s:else>
</table>
<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; padding-left:10px;">关联交易</div>
<div id="lessGridList13" style="overflow: auto; width: 100%;">
<table class="lessGrid" cellspacing="0"	width="100%" align="center" cellpadding="0">
	<tr>
		<th style="text-align: center">交易编号</th>
		<th style="text-align: center">金额_人民币</th>
		<th style="text-align: center">税额_人民币</th>
		<th style="text-align: center">供应商ID</th>
		<th style="text-align: center">交易发生机构</th>
	</tr>
	<s:if test='transList == null || transList.size()==0'>
	<tr><td colspan="5">该发票没有对应的商品信息！</td></tr>
	</s:if>
	<s:else>
	<s:iterator value="transList" id="itList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td><s:property value='dealNo'/></td>
			<td><s:property value='amtCny'/></td>
			<td><s:property value='taxAmtCny'/></td>
			<td><s:property value='vendorId'/></td>
			<td><s:property value='bankName'/></td>
		</tr>
	</s:iterator> 
	</s:else>
</table>
</div>
<div id="ctrlbutton" style=" text-align:right; padding:10px 5px 10px 0;">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>
</form>
</div>
</body>

</html>