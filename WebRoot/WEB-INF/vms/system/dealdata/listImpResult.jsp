<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>价税分离查询</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("conditionForm");
		form.action = actionUrl;
		form.submit();
		form.action = "searchListImpResult.action";
	}
</script>
</head>
<body>
<form id="conditionForm" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">发票管理</span>
			<span class="current_status_submenu">价税分离</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td width="50">导入时间</td>
				<td width="150"><input id="startTime" name="impTime" type="text"
					value="<s:property value='impTime' />" class="tbl_query_time"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
				<td><input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="submitForm('searchListImpResult.action');"
					name="cmdFilter" value="查询" id="cmdFilter" /></td>
				<!-- <td><input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="submitForm('expImpTransResult.action');"
					name="cmdFilter" value="导出" id="cmdFilter" /></td> -->
			</tr>
		</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<a href="#" onclick="submitForm('expImpTransResult.action');"	name="cmdFilter" id="cmdFilter" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
			</td>
		</tr>
	</table>
	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"
			cellpadding="0" style="border-collapse: collapse;width: 100%;">
			<tr class="lessGrid head">

				<th style="text-align: center">序号</th>
				<th style="text-align: center">导入日期</th>
				<th style="text-align: center">终端号</th>
				<th style="text-align: center">交易ID</th>
				<th style="text-align: center">客户ID</th>
				<th style="text-align: center">交易时间</th>
				<th style="text-align: center">金额_人民币</th>
				<th style="text-align: center">税额_人民币</th>
				<th style="text-align: center">收入_人民币</th>
				<th style="text-align: center">增值税类型</th>
				<th style="text-align: center">税率</th>
				<th style="text-align: center">交易类型</th>
				<th style="text-align: center">交易种类</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList"
				status="stuts">
				<tr align="center"
					class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<td><s:property value='#stuts.count' /></td>
					<td><s:property value='imptime' /></td>
					<td><s:property value='terminal' /></td>
					<td><s:property value='transId' /></td>
					<td><s:property value='customerId' /></td>
					<td><s:property value='transactionDate' /></td>
					<td align="right"><s:property value='amtCny' /></td>
					<td align="right"><s:property value='taxAmtCny' /></td>
					<td align="right"><s:property value='incomeCny' /></td>
					<td><s:property value='vatRateCode' /></td>
					<td align="right"><s:property value='taxRate' /></td>
					<td><s:property value='productIeType' /></td>
					<td><s:property value='ieItem' /></td>
				</tr>
			</s:iterator>
		</table>


	</div>
	<div id="anpBoud" align="Right" style="width:100%;">
		<table width="100%" cellspacing="0" border="0">
			<tr>
				<td align="right"><s:component template="pagediv" /></td>
			</tr>
		</table>
	</div>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>