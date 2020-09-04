<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.AutoInvoiceParam"
	import="com.cjit.vms.trans.util.DataUtil"
	import="cjit.crms.util.DateUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function submit() {
			document.forms[0].submit();
		}
		$(function() {
			$("#goodsRel").click(function() {
				var ids = $(".selectTransTypeIds:checked");
				var size = ids.size();
				var keys = "";
				if (size == 0) {
					alert("请选择交易类型");
					return;
				}
				for (var i = 0; i < size; i++) {
					if (i != size - 1) {
						keys = keys + ids[i].value + ",";
					} else {
						keys = keys + ids[i].value;
					}
				}
				var url = "relTrans2Goods.action" + "?ids=" + keys;
				OpenModalWindow(encodeURI(url), 600, 650, true);
			});
			$("#addBtn").click(function() {
				//var url = "editTransType.action";
				var url = "editParamAutoInvoice.action";
				OpenModalWindow(encodeURI(url), 500, 450, true);
			});
			$("#cmdDelbt").click(function() {
				submitForm("deleteParamAutoInvoice.action");
			});
			function submitForm(actionUrl) {
				var form = $("#main");
				var oldAction = form.attr("action");
				form.attr("action", actionUrl);
				form.submit();
				form.attr("action", oldAction);
			}
		})
	</script>
	<style type="text/css">
		.style1 {
		width: 242px;
	}
	</style>
</head>
<body>
<form id="main" action="listParamAutoInvoice.action" method="post">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100" width="80%">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
			<span class="current_status_menu">当前位置：</span>
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">自动开票管理</span>
			<span class="current_status_submenu">自动开票管理</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" >
		<tr>
			<td align="left">客户纳税识别号:</td>
			<td>
				<input type="text" class="tbl_query_text" name="aipCondition.custTaxNo" 
					value="<s:property value='aipCondition.custTaxNo'/>" />
			</td>
			<td align="left">业务种类:</td>
			<td>
				<s:select name="aipCondition.bussType" list="bussTypeList" 
					listKey='valueStandardNum' listValue='name' headerKey="" headerValue="所有" />
			</td>
			<td align="left">费用类型:</td>
			<td>
				<s:select name="aipCondition.costType" list="costTypeList" 
					listKey='valueStandardNum' listValue='name' headerKey="" headerValue="所有" />
			</td>
		</tr>
		<tr>
			<td align="left">缴费频率:</td>
			<td>
				<s:select name="aipCondition.payFreq" list="payFreqList" 
					listKey='valueStandardNum' listValue='name' headerKey="" headerValue="所有" />
			</td>
			<td align="left">发票类型:</td>
			<td>
				<s:select name="aipCondition.invoiceType" list="invoiceTypeList" 
					listKey='valueStandardNum' listValue='name' headerKey="" headerValue="所有" />
			</td>
			<td style="text-align:left" colspan="2">
				<input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					onclick="submit();" name="cmdSelect" value="查询" id="cmdSelect" />
			</td>
		</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<a href="#" id="addBtn" name="cmdFilter" id="cmdFilter">
					<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />新增</a>
				<a href="#" id="cmdDelbt" name="cmdDelbt" id="cmdDelbt">
					<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />删除</a>
			</td>
		</tr>
		</table>
		</div>
		<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
		<tr class="lessGrid head">
			<th style="text-align:center;">
				<input id="CheckAll" type="checkbox" onclick="cbxselectall(this,'checkedlineNo')" />
			</th>
			<th style="text-align:center" width="05%">序号</th>
			<th style="text-align:center">客户纳税识别号</th>
			<th style="text-align:center">客户名称</th>
			<th style="text-align:center">业务种类</th>
			<th style="text-align:center">费用类型</th>
			<th style="text-align:center">缴费频率</th>
			<th style="text-align:center">发票类型</th>
			<th style="text-align:center">周年日</th>
			<th style="text-align:center">年度</th>
			<th style="text-align:center">期数</th>
			<th style="text-align:center">特殊标记</th>
			<th style="text-align:center" width="20%">备注</th>
			<!-- <th style="text-align:center" width="15%">生效日期范围</th> -->
			<th style="text-align:center" width="05%">操作</th>
		</tr>
	<%
		List paramList = (List)request.getAttribute("paramList");
		if (paramList != null && paramList.size() > 0){
			for (int i = 0; i < paramList.size(); i++){
				AutoInvoiceParam aip = (AutoInvoiceParam)paramList.get(i);
				if (i % 2 == 0){
	%>
		<tr class="lessGrid rowA" id="<%=aip.getParamId()%>" >
	<%
				}else{
	%>
		<tr class="lessGrid rowB" id="<%=aip.getParamId()%>" >
	<%
				}
	%>
			<td align="center" style="width:30px;">
				<input style="width:13px;height:13px;" type="checkbox" class="selectParamIds" name="checkedlineNo" value="<%=aip.getParamId()%>" />
			</td>
			<td align="center"><%=i+1%></td>
			<td align="center"><%=aip.getCustTaxNo()%></td>
			<td align="left"><%=aip.getCustName()%></td>
			<td align="center"><%=aip.getBussType()%></td>
			<td align="center"><%=aip.getCostType()%></td>
			<td align="center"><%=aip.getPayFreq()%></td>
			<td align="center"><%=aip.getInvoiceType()%></td>
			<td align="center"><%=aip.getWeekYearDay()%></td>
			<td align="center"><%=aip.getAnnual()%></td>
			<td align="center"><%=aip.getPeriods()%></td>
			<td align="center"><%=aip.getSpecialMark()%></td>
			<td align="left"><%=aip.getRemark()%></td>
			<!-- <td align="center"><%//=aip.getBeginDate()%>～<%//=aip.getEndDate()%></td> -->
			<td align="center">
				<a href="#" onClick="OpenModalWindow('editParamAutoInvoice.action?paramId=<%=aip.getParamId()%>',500,450,true)">
				<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
			</td>
	<%
			}
		}
	%>
		</table>
		</div>
		<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
			<table width="100%" cellspacing="0" border="0">
				<tr>
					<td align="right">
						<s:component template="pagediv"/>
					</td>
				</tr>
			</table>
		</div>
		</td>
	</tr>
	</table>
</form>
</body>
</html>