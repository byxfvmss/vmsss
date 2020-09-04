<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.input.model.InputInvoiceItem"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.input.model.InputTrans"
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
		List transList = (ArrayList)request.getAttribute("transList");
		String billId = (String)request.getAttribute("billId");
		if (billId == null){
			billId = "";
		}
	%>
	<input type="hidden" name="billId" value="<%=billId%>"/>

	<div id="lessGridList17" style="overflow: auto; width: 100%;">
	<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th>交易编号</th>
		<th>金额_人民币</th>
		<th>税额_人民币</th>
		<th>供应商ID</th>
		<th>交易发生机构</th>
	</tr>
	<%
		for(int i = 0; transList != null && i < transList.size(); i++){
			InputTrans item = (InputTrans) transList.get(i);
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

		<td align="center"><%=item.getDealNo()%></td>
		<td align="center"><%=item.getAmtCny()==null?"":item.getAmtCny()%></td>
		<td align="center"><%=item.getTaxAmtCny()==null?"":item.getTaxAmtCny()%></td>
		<td align="center"><%=item.getVendorId()==null?"":item.getVendorId()%></td>
		<td align="center"><%=item.getBankCode()==null?"":item.getBankCode()%></td>
	</tr>
	<%
		}
	%>
	</table>
    </div> 
    </form>
</body>
</html>