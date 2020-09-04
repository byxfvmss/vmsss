<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">

<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>完整性对账</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =       90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
	}
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listDistrubute.action"
		method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">税项监控</span> <span
							class="current_status_submenu">增值税对账</span> <span
							class="current_status_submenu">完整性对账</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="60">机构</td>
								<td width="180"><input type="hidden" id="instCode"
									name="instCode"
									value='<s:property value="invoiceDistribute.receiveInstId"/>'>
									<input type="text" class="tbl_query_text" id="receiveInstName"
									name="invoiceDistribute.receiveInstName"
									value='<s:property value="invoiceDistribute.receiveInstName"/>'
									onclick="setOrg(this, '#receiveInstId', '#receiveInstName');"
									readonly="readonly"></td>
								<td colspan="2"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listIntegrityCheckAccount.action');"
									name="cmdSelect" value="查询" id="cmdSelect" /></td>
							</tr>
						</table>
					</div>
					<div id="lessGridList4" class="mtop10"
						style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center"><input
									style="width:13px;height:13px;" id="CheckAll" type="checkbox"
									onclick="cbxselectall(this,'selectBillIds')" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">机构</th>
								<th style="text-align: center">金额</th>
								<th style="text-align: center">净价</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">差异</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox" style="width:13px;height:13px;"
										name="selectBillIds" id="selectBillIds" 
										value="<s:property value="#iList.instCode"/>"
									</td>
									<td><s:property value='#stuts.count' /></td>
									<td><s:property value='instName' /></td>
									<td><s:property value='amount' /></td>
									<td><s:property value='netPrice' /></td>
									<td><s:property value='taxAmt' /></td>
									<td><s:property value='devAmt' /></td>
									<td>
									<a href="integrityCheckAccountType.action?instCode=<s:property value='#iList.instCode' />">明细</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;vlign=top;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="left"></td>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include_wiethCheckAccount.jsp"
		charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="receiveInstName" />
		<c:param name="bankId_tree" value="instCode" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
<script type="text/javascript">
	$(function() {
		$(".history_bt")
				.click(
						function() {
// 							submitForm("<c:out value='${webapp}'/>/integrityCheckAccountType.action");
						});
	})
</script>
</html>