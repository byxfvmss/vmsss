<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.input.model.InputInvoiceItem"
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
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <style type="text/css">
        .style1
        {
            width: 242px;
        }
    </style>
</head>
<body class="white" onLoad="loadBillAmtInfo();" onmousemove="MM(event)" onmouseout="MO(event)">
    <form name="Form1" method="post" action="listDatasInner.action" id="Form1" enctype="multipart/form-data">
	<%
		Map innerCheckResult = (HashMap)session.getAttribute(ScopeConstants.CHECK_RESULT_INNER);
		List billItemList = (ArrayList)request.getAttribute("billItemList");
		String billId = (String)request.getAttribute("billId");
		if (billId == null){
			billId = "";
		}
	%>
	<input type="hidden" name="billId" value="<%=billId%>"/>

	<div id="lessGridList17" style="overflow: auto; width: 100%;">
	<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th>商品名称</th>
		<th>商品数量</th>
		<th>商品单价</th>
		<th>金额</th>
		<th>税率</th>
		<th>税额</th>
	</tr>
	<%
		for(int i = 0; billItemList != null && i < billItemList.size(); i++){
			InputInvoiceItem billItem = (InputInvoiceItem) billItemList.get(i);
			if(i%2==0){
	%>
	<tr class="row1">
	<%
			}else{
	%>
	<tr class="row2">
	<%
			}
	%>

		<td align="center"><%=billItem.getGoodsName()%></td>
		
		<!--商品数量-->
		<td align="center"><%=billItem.getGoodsNo()==null?"":billItem.getGoodsNo()%></td>
		<!--商品单价-->
		<td align="center"><%=billItem.getGoodsPrice()==null?"":billItem.getGoodsPrice()%></td>
		<!--金额-->
		<td align="center"><%=billItem.getAmt()%></td>
		<!--税率-->
		<td align="center"><%=billItem.getTaxRate()==null?"":billItem.getTaxRate()%></td>
		<!--税额-->
		<td align="center"><%=billItem.getTaxAmt()%></td>
	</tr>
	<%
		}
	%>
	</table>
    </div> 
    </form>
</body>
</html>