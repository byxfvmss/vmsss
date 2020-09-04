<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script>
	function submit() {
		document.forms[0].submit();
	}
	function reTr(t){
		$(t).parents("tr:first").remove();
	}
</script>
</head>
<!--<body class="white" onLoad="loadBillAmtInfo();" onmousemove="MM(event)" onmouseout="MO(event)">-->
<body class="white" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="listDatasInner.action"
		id="Form1" enctype="multipart/form-data">
		<%
			Map innerCheckResult = (HashMap) session
					.getAttribute(ScopeConstants.CHECK_RESULT_INNER);
			List billItemList = (ArrayList) request
					.getAttribute("billItemList");
			String billId = (String) request.getAttribute("billId");
			String updFlg = (String) request.getAttribute("updFlg");
			String taxno = (String) request.getAttribute("taxno");
			String fapiaoType = (String) request.getAttribute("fapiaoType");
			if (billId == null) {
				billId = "";
			}
		%>
		<input type="text" name="billId" value="<%=billId%>" /> <input
			type="text" name="updFlg" value="<%=updFlg%>" /> <input
			type="text" name="taxno" value="<%=taxno%>" /> <input type="text"
			name="fapiaoType" value="<%=fapiaoType%>" />

		<table class="lessGrid" cellspacing="0" rules="all" border="0"
			cellpadding="0" display="none" id="billItemPreListYS" 
			style="border-collapse: collapse; width: 100%;">
			<tr class="lessGrid head" height="30">
				<!-- 		<th style="text-align:center" width="4%">修改</th> -->
				<th style="text-align: center" width="140px;">商品名称</th>
				<th style="text-align: center" width="100px;">规格型号</th>
				<th style="text-align: center" width="100px;">单位</th>
				<th style="text-align: center" width="100px;">金额</th>
				<th style="text-align: center" width="50px;">数量</th>
				<th style="text-align: center" width="100px;">单价</th>
				<th style="text-align: center" width="50px;">税率</th>
				<th style="text-align: center" width="10px;">税额</th>
				<th style="text-align:center" width="4%">删除</th>
			</tr>
			<%
				for (int i = 0; billItemList != null && i < billItemList.size(); i++) {
					BillItemInfo billItem = (BillItemInfo) billItemList.get(i);
					if (i % 2 == 0) {
			%>
			<tr class="lessGrid row1">
				<%
					} else {
				%>
			
			<tr class="lessGrid row2">
				<%
					}
				%>
				<!--商品名称-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text5" value="<%=billItem.getGoodsName()%>"
					disabled="disabled" style="text-align: center;" />
				</td>
				<!--规格型号-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text3" value="<%=billItem.getSpecandmodel()%>" style="text-align: center;" />
				</td>
				<!--单位-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text6" value="<%=billItem.getGoodsUnit()%>" style="text-align: center;" />
				</td>
				<!--金额-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text3" value="<%=billItem.getAmt()%>" style="text-align: center;" />
				</td>
				<!--商品数量-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text4"
					value="<%=billItem.getGoodsNo() == null ? "" : billItem
						.getGoodsNo()%>" style="text-align: center;" />
				</td>
				<!--商品单价-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text3"
					value="<%=billItem.getGoodsPrice() == null ? "" : billItem
						.getGoodsPrice()%>" style="text-align: center;" disabled="disabled" />
				</td>
				<!--税率-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text4"
					value="<%=billItem.getTaxRate() == null ? "" : billItem
						.getTaxRate()%>" style="text-align: center;" disabled="disabled" />
				</td>
				<!--税额-->
				<td style="text-align: center"><input type="text"
					class="tbl_query_text3" value="<%=billItem.getTaxAmt()%>" style="text-align: center;" disabled="disabled" />
				</td>
				<td style="text-align: center"><a onclick="reTr(this)"> <img
						src="<%=bopTheme2%>/img/jes/icon/delete.png" title="删除明细"
						style="border-width: 0px;" /> </a></td>
			</tr>
			<%
				}
			%>
		</table>
	</form>
</body>
</html>