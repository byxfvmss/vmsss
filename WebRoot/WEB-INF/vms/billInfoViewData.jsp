<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
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
	<tr class="header">
		<th colspan="4">票据查看</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">申请开票日期:</td>
		<td width="35%"><s:property value="billInfo.applyDate" /></td>
		<td width="15%" style="text-align: right" class="listbar">开票日期:</td>
		<td><s:property value="billInfo.billDate" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
		<td width="35%">
			<s:iterator value="mapIssueType" id="entry">  
				<s:if test='billInfo.fapiaoType==#entry.key'><s:property value="value"/></s:if>
			</s:iterator>
		</td>
		<td width="15%" style="text-align: right" class="listbar">客户名称</td>
		<td width="35%"><s:property value="billInfo.customerName" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户纳税人识别号:</td>
		<td width="35%"><s:property value="billInfo.customerTaxno" /></td>
		<td width="15%" style="text-align: right" class="listbar">客户地址电话:</td>
		<td><s:property value="billInfo.customerAddressandphone" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户开户行及账号:</td>
		<td width="35%"><s:property value="billInfo.customerBankandaccount" /></td>
		<td width="15%" style="text-align: right" class="listbar">合计金额:</td>
		<td><s:property value="billInfo.amtSum" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">合计税额:</td>
		<td width="35%"><s:property value="billInfo.taxAmtSum" /></td>
		<td width="15%" style="text-align: right" class="listbar">价税合计:</td>
		<td><s:property value="billInfo.sumAmt" /></td>
	</tr>
</table>
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
</table>
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0" style="margin-top:20px;">
	<tr class="header">
		<th colspan='13'>已勾稽的数据</th>
	</tr>
	<tr >
		<th style="text-align: center">序号</th>
		<th style="text-align: center">交易时间</th>
		<th style="text-align: center">客户名称</th>
		<th style="text-align: center">交易类型</th>
		<th style="text-align: center">交易金额</th>
		<th style="text-align: center">税率</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">收入</th>
		<th style="text-align: center">价税合计</th>
		<th style="text-align: center">未票据金额</th>
		<th style="text-align: center">发票类型</th>
		<th style="text-align: center">交易状态</th>
	</tr>
	<s:iterator value="checkYList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='transDate'/></td>
			<td><s:property value='customerName'/></td>
			<td><s:property value='transTypeName'/></td>
			<td><s:property value='amt'/></td>
			<td><s:property value='taxRate'/></td>
			<td><s:property value='taxAmt'/></td>
			<td><s:property value='income'/></td>
			<td>
				<s:if test='taxFlag=="Y"'>
					<s:property value='amt'/>
				</s:if>
				<s:if test='taxFlag=="N"'>
					<s:property value='amt+taxAmt'/>
				</s:if>
			</td>
			<td><s:property value='balance'/></td>
			<td>
				<s:iterator value="mapVatType" id="entry">  
					<s:if test='fapiaoType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td><s:property value='mapDataStatus[#iList.dataStatus]'/></td>
		</tr>
	</s:iterator> 
</table>
</div>


<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>