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
<title>交易查询对应票据信息</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>

</head>
<body class="white">
<form id="frm" method="post">
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:15px; font-weight:bold;">交易查询对应票据信息</div>
<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<td width="15%" align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">交易时间:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;">
		<s:property value='informationInput.transDate'/> 
		</td>
		<td width="15%" align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">交易机构:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;">
		<s:property value='informationInput.bankName'/> 
		</td>
		
	</tr>
	<tr>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">交易金额:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;">
		<s:property value='informationInput.amtCny'/>
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">交易编号:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;">
		<s:property value='informationInput.dealNo'/> 
		</td>
	</tr>
	
	<tr>
	<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">税额:&nbsp;&nbsp;&nbsp;</td>
	<td colspan="3" style="background-color:#F0F0F0;"><s:property value='informationInput.taxAmtCny'/></td>
	</tr>
</table>
<div id="lessGridList15" style="overflow:auto;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr>
				<th style="text-align:center">发票代码</th>
				<th style="text-align:center">发票号码</th>
				<th style="text-align:center">开票日期</th>
				<th style="text-align:center">所属机构</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">税额</th>
				<th style="text-align:center">发票类型</th>
				<th style="text-align:center">供应商名称</th>
				<th style="text-align:center">供应商纳税人识别号</th>
				<th style="text-align:center">认证日期</th>
				<th style="text-align:center">扫描时间</th>
			</tr>
	<s:iterator value="billsList" id="iList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td><s:property value='billCode'/></td>
			<td><s:property value='billNo'/></td>
			<td><s:property value='billDate'/></td>
			<td><s:property value='instName'/></td>
			<td><s:property value='amtSum'/></td>
			<td><s:property value='taxAmtSum'/></td>
			<td>
				<s:iterator value="mapVatType" id="entry">  
					<s:if test='faPiaoType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td><s:property value='vendorName'/></td>
			<td><s:property value='vendorTaxNo'/></td>
			<td><s:property value='identifyDate'/></td>
			<td><s:property value='scanDate'/></td>
		</tr>
	</s:iterator> 
</table>
</div>
<table id="tbl_tools" width="100%" border="0">
	<tr>
		<td align="left">
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>